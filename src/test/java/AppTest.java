import com.cecs.SongServices;
import com.cecs.UserServices;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class AppTest {
    @Test
    void loadSong() throws IOException {
        var ret0 = SongServices.getSongChunk("SOMZWCG12A8C13C480", 0L);
        var ret1 = SongServices.getSongChunk("SOMZWCG12A8C13C480", 1L);
        System.out.println(ret0.substring(0, 40));
        System.out.println(ret1.substring(0, 40));
    }

    @Test
    void checkSize() {
        var ret = SongServices.getFileSize("SOMZWCG12A8C13C480");
        System.out.format("Size of file: %s bytes\n", ret);
    }

    @Test
    void loginUser() throws IOException {
        var json1 = new JsonObject();
        var json2 = new JsonObject();
        var json3 = new JsonObject();
        var gson = new GsonBuilder().setPrettyPrinting().create();

        var ret1 = UserServices.login("chris", "greer");
        json1.addProperty("ret", gson.toJson(ret1));
        System.out.println("Ret 1\n" + json1.toString());

        var ret2 = UserServices.login("new", "geer");
        json2.addProperty("ret", gson.toJson(ret2));
        System.out.println("Ret 2\n" + json2.toString());

        var ret3 = UserServices.login("invalid", "entry");
        json3.addProperty("ret", gson.toJson(ret3));
        System.out.println("Ret 3\n" + json3.toString());
    }
}
