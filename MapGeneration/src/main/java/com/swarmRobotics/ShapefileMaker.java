package com.swarmRobotics;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.UIManager;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class ShapefileMaker {

    private List<SimpleFeature> extractFeatures(Cell[][] maze, int width, int height, int blockSize, SimpleFeatureType BLOCK_TYPE){
        List<SimpleFeature> features = new ArrayList<>();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(BLOCK_TYPE);
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if (maze[i][j].getType() != Type.WALL){
                    int leftTopX = j*blockSize, leftTopY = i*blockSize;
                    Coordinate[] coords  =
                            new Coordinate[] {new Coordinate(leftTopX, leftTopY), new Coordinate(leftTopX + blockSize, leftTopY),
                                    new Coordinate(leftTopX + blockSize, leftTopY + blockSize), new Coordinate(leftTopX, leftTopY + blockSize),
                                    new Coordinate(leftTopX, leftTopY) };
                    LinearRing ring = geometryFactory.createLinearRing( coords );
                    LinearRing holes[] = null; // use LinearRing[] to represent holes
                    Polygon polygon = geometryFactory.createPolygon(ring, holes );
                    //System.out.println(polygon);
                    featureBuilder.add(polygon);
                    featureBuilder.add(0);
                    SimpleFeature polygonFeature = featureBuilder.buildFeature(null);
                    features.add(polygonFeature);
                }

            }
        }
        return features;
    }
    public void makeShapefile(Cell[][] maze, int width, int height) throws IOException, SchemaException {

        final SimpleFeatureType BLOCK_TYPE = DataUtilities.createType("Block",
                "the_geom:MultiPolygon," + // <- the geometry attribute: Point type
                        "id:Integer"   // a number attribute
        );
//        List<SimpleFeature> features = new ArrayList<>();
////        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
////        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(BLOCK_TYPE);
////        Coordinate[] coords  =
////                new Coordinate[] {new Coordinate(4, 0), new Coordinate(2, 2),
////                        new Coordinate(4, 4), new Coordinate(6, 2), new Coordinate(4, 0) };
////
////        LinearRing ring = geometryFactory.createLinearRing( coords );
////        LinearRing holes[] = null; // use LinearRing[] to represent holes
////        Polygon polygon = geometryFactory.createPolygon(ring, holes );
////        System.out.print(polygon);
////        featureBuilder.add(polygon);
////        featureBuilder.add(0);
////        SimpleFeature polygonFeature = featureBuilder.buildFeature(null);
////        features.add(polygonFeature);

        List<SimpleFeature> features = extractFeatures(maze, width, height,10, BLOCK_TYPE);
        File newFile = new File("shapefile.shp");

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", newFile.toURI().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);

        /*
         * TYPE is used as a template to describe the file contents
         */
        newDataStore.createSchema(BLOCK_TYPE);
        /*
         * Write the features to the shapefile
         */
        Transaction transaction = new DefaultTransaction("create");

        String typeName = newDataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);
        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();
        /*
         * The Shapefile format has a couple limitations:
         * - "the_geom" is always first, and used for the geometry attribute name
         * - "the_geom" must be of type Point, MultiPoint, MuiltiLineString, MultiPolygon
         * - Attribute names are limited in length
         * - Not all data types are supported (example Timestamp represented as Date)
         *
         * Each data store has different limitations so check the resulting SimpleFeatureType.
         */
        System.out.println("SHAPE:"+SHAPE_TYPE);

        if (featureSource instanceof SimpleFeatureStore) {
            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
            /*
             * SimpleFeatureStore has a method to add features from a
             * SimpleFeatureCollection object, so we use the ListFeatureCollection
             * class to wrap our list of features.
             */
            SimpleFeatureCollection collection = new ListFeatureCollection(BLOCK_TYPE, features);
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(collection);
                transaction.commit();
            } catch (Exception problem) {
                problem.printStackTrace();
                transaction.rollback();
            } finally {
                transaction.close();
            }
            System.exit(0); // success!
        } else {
            System.out.println(typeName + " does not support read/write access");
            System.exit(1);
        }

    }
}
