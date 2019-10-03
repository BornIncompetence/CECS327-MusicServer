import com.cecs.SongServices;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.cecs.App.openConnection;

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
}
