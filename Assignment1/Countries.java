import java.io.*;
import java.util.StringTokenizer;

public class Countries {
    private String country;
    private double lat;
    private double lng;
    public static final int MAX_SIZE = 1024;

    public Countries(String country) {
        try {
            FileReader countries_csv = new FileReader("Countries.csv");
            BufferedReader br = new BufferedReader(countries_csv);
            String[][] countries = new String[MAX_SIZE][3];
            int index = 0;

            String readLine = br.readLine();
            while (readLine != null) {
                StringTokenizer st = new StringTokenizer(readLine, ",");
                countries[index][0] = st.nextToken();
                countries[index][1] = st.nextToken();
                countries[index++][2] = st.nextToken();
                readLine = br.readLine();
            }

            for (String[] temp : countries) {
                if (temp[0].equals(country)) {
                    this.country = country;
                    lat = Double.parseDouble(temp[1]);
                    lng = Double.parseDouble(temp[2]);
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCountry() { return country; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
}