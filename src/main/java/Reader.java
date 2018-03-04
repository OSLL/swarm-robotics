import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private double euclid_distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public List<Point> get_nearby_cars(Point main_car, double radius, SimpleFeatureCollection collection){
        List<Point> nearby_cars = new ArrayList<>();
        try(SimpleFeatureIterator iterator = collection.features()) {
            while( iterator.hasNext() ){
                SimpleFeature feature = iterator.next();
                Long is_car = (Long) feature.getAttribute("is_car");
                Point point = (Point) feature.getDefaultGeometry();

                if (is_car == 1 && point.getX() != main_car.getX() && point.getY() != main_car.getY()){
                    double distance = euclid_distance(point.getX(), point.getY(), main_car.getX(), main_car.getY());

                    if (distance <= radius)
                        nearby_cars.add(point);
                }
            }
        }
        return nearby_cars;
    }
}
