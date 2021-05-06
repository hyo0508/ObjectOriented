public class Distance {
    private String name;
    private double lat;
    private double lng;

    public Distance(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    //public String getName() { return name; }
    public double getLat() { return lat; } //There's no
    public double getLng() { return lng; } //There's no

    public String writeDistance() {
        return "Country : " + name +
                "\nlatitude = " + lat +
                "\nlongitude = " + lng +
                "\n--------------------\n";
    }
    public static String getDistance(Distance a, Distance b) {
        double distance = Math.sqrt(Math.pow(a.getLng() - b.getLng(), 2) + Math.pow(a.getLat() - b.getLat(), 2));
        return "Distance of\n" + a.writeDistance() + b.writeDistance() + "is\n" + distance;
    }
}