import java.io.*;
import java.util.Calendar;

public class TravelInfoRequest {
    public static final int MAX_SIZE = 1024;
    public static void main(String[] args) throws IOException {
        FileReader template = new FileReader("template_file.txt");
        FileReader properties = new FileReader("properties.txt");
        BufferedReader br = new BufferedReader(properties);
        //FileReader countries = new FileReader("Countries.csv");
        FileWriter output = new FileWriter("output.txt");
        int index = 0;

        KeyValue[] keyValues = new KeyValue[10];
        String readLine = br.readLine();
        while (readLine != null) {
            keyValues[index++] = new KeyValue(readLine);
            readLine = br.readLine();
        }
        Calendar now = Calendar.getInstance();
        String date = "" + now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE);
        keyValues[index] = new KeyValue("date", date);

        int read;
        byte[] bytes = new byte[MAX_SIZE];
        index = 0;
        while ((read = template.read()) != -1) {
            if (read == '{') {
                while ((read = template.read()) != '}') {
                    bytes[index++] = (byte)read;
                }
                String key = new String(bytes, 0, index);
                for (KeyValue keyValue : keyValues) {
                    if (keyValue == null) break;
                    if (key.equals(keyValue.getKey())) {
                        output.write(keyValue.getValue());
                        break;
                    }
                }
                bytes = new byte[MAX_SIZE];
                index = 0;
            }
            else if (read == '<') {
                while ((read = template.read()) != '>') {}
                String startCountry = null, departCountry = null;
                for (KeyValue keyValue : keyValues) {
                    if (keyValue == null) break;
                    if ("startcountry".equals(keyValue.getKey())) {
                        startCountry = keyValue.getValue();
                    }
                    if ("departcountry".equals(keyValue.getKey())) {
                        departCountry = keyValue.getValue();
                    }
                }
                Countries a = new Countries(startCountry);
                Countries b = new Countries(departCountry);
                Distance startDistance = new Distance(a.getCountry(), a.getLat(), a.getLng());
                Distance departDistance = new Distance(b.getCountry(), b.getLat(), b.getLng());
                output.write(Distance.getDistance(startDistance, departDistance));
            }
            else {
                output.write(read);
            }
        }
        output.close();
    }
}