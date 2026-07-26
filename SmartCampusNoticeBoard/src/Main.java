import menu.Menu;
import repository.NoticeRepository;
import repository.UserRepository;
import service.AuthService;
import service.NoticeService;
import util.ExpiryChecker;

public class Main {
    public static void main(String[] args) {
        NoticeRepository noticeRepository = new NoticeRepository();
        UserRepository userRepository = new UserRepository();

        NoticeService noticeService = new NoticeService(noticeRepository);
        AuthService authService = new AuthService(userRepository);

        ExpiryChecker expiryChecker = new ExpiryChecker(noticeRepository, 60000);
        expiryChecker.start();

        Menu menu = new Menu(authService, noticeService);
        menu.start();

        expiryChecker.stopChecking();
    }
}
