package in.technodroid.model;

/**
 * Created by IBM_ADMIN on 9/15/2015.
 */
import org.json.JSONObject;

import java.util.List;

/**
 * Created by kstanoev on 1/14/2015.
 */
public interface AsyncResult
{
    void onResult(List<?> object);
}