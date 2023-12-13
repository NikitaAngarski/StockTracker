import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFilter {
    public static Map<String, DataItem> filterByDate(Map<String, DataItem> items, String startDate, String endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return items.entrySet().stream()
                    .filter(entry -> {
                        try {
                            Date date = format.parse(entry.getKey());
                            Date start = format.parse(startDate);
                            Date end = format.parse(endDate);
                            return date.after(start) && date.before(end);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
