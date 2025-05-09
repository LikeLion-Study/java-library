package library;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApplicationTest {
    private final java.io.InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    public void 도서_조회_기능_테스트() throws Exception {
        String input = "조회 Do it\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        Application.main(new String[]{});

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("총 2권의 도서가 검색되었습니다.");
        assertThat(output).contains("[ISBN: 987-89A02001]").contains("\"Do it! 자료구조\"");
        assertThat(output).contains("[ISBN: 987-89A02008]").contains("\"Do it! 알고리즘\"");
    }

    @Test
    public void 대출_기능_오류_테스트() throws Exception {
        String input = "대출 987-89A02035\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        Application.main(new String[]{});

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("[ERROR]").contains("이미 대여 중입니다.");
    }

    @Test
    public void 대출_기능_정상_테스트() throws Exception {
        String input = "대출 Do it! 알고리즘\nY\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        Application.main(new String[]{});

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("[INFO] \"Do it! 알고리즘\" 대출이 완료되었습니다.");
        assertThat(output).contains("대출일:");
        assertThat(output).contains("반납일:");
    }

    @Test
    public void 반납_기능_정상_테스트() throws Exception {
        String input = "반납 987-89A01066\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        Application.main(new String[]{});

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("[INFO] 요청하신 도서:");
        assertThat(output).contains("대출일:");
        assertThat(output).contains("반납일:");
        assertThat(output).contains("연체료:");
    }

    @Test
    public void 반납_기능_오류_테스트() throws Exception {
        String input = "반납 987-89A01005\nN\n";
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));

        Application.main(new String[]{});

        String output = outputStream.toString(StandardCharsets.UTF_8);
        assertThat(output).contains("[ERROR]").contains("대여 기록이 없습니다");
    }
}