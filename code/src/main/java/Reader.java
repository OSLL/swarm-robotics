import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.sun.org.apache.xpath.internal.operations.Bool;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.*;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

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
        MapContent map_ = new MapContent();
        map_.setTitle("Quickstart");

        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(featureSource, style);
        map_.addLayer(layer);

        // Now display the map
        JMapFrame.showMap(map_);

        //File file = new File("example.shp");

        //get data from file
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
                .getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection_ = source.getFeatures(filter);
        //SimpleFeatureCollection featureCollection = source.getFeatures(filter);
        try (FeatureIterator<SimpleFeature> features = collection_.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                System.out.print(feature.getID());
                System.out.print(": ");
                System.out.print(feature.getDefaultGeometryProperty().getValue());
                List<Point> nearbyCars = get_nearby_cars((Point)feature.getDefaultGeometry(), 5, collection_);
                System.out.println(" nearby cars : " + nearbyCars.size());
            }
        }
    }

    private static double euclid_distance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    public static List<Point> get_nearby_cars(Point main_car, double radius, FeatureCollection<SimpleFeatureType, SimpleFeature> collection){
        List<Point> nearby_cars = new ArrayList<>();
        try(FeatureIterator<SimpleFeature> iterator = collection.features()) {
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
