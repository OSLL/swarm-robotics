import java.io.File;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class Reader {
    public static void main(String[] args) throws Exception {
        // display a data store file chooser dialog for shapefiles
        File file = JFileDataStoreChooser.showOpenFile("shp", null);
//        File file =
        if (file == null) {
            return;
        }

        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();

        SimpleFeatureType SHAPE_TYPE = featureSource.getSchema();

        System.out.println("SHAPE:"+SHAPE_TYPE);

        SimpleFeatureCollection collection = featureSource.getFeatures();
        SimpleFeatureIterator iterator = collection.features();
        try {
            while( iterator.hasNext() ){
                SimpleFeature feature = iterator.next();
                Long is_car = (Long) feature.getAttribute("is_car");
                Point point = (Point) feature.getDefaultGeometry();
                System.out.println(is_car);
//                System.out.print(point.getX());
//                System.out.println(point.getY());

            }
        }
        finally {
            iterator.close();
        }
//        System.out.println(featureSource.getFeatures());
        // Create a map content and add our shapefile to it
//        MapContent map = new MapContent();
//        map.setTitle("Quickstart");
//
//        Style style = SLD.createSimpleStyle(featureSource.getSchema());
//        Layer layer = new FeatureLayer(featureSource, style);
//        map.addLayer(layer);
//
//        // Now display the map
//        JMapFrame.showMap(map);
    }
}
