package service;

import dao.UserDAO;
import model.User;
import util.HashUtil;

/**
 * Kullanıcı adı + şifre ile giriş doğrulamasını yapan servis.
 * DB erişimini UserDAO üzerinden yapar, şifre doğrulamasını HashUtil.verify ile kontrol eder.
 */
public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Login denemesi yapar.
     *
     * @param username      Kullanıcı adı (boş / null ise direkt başarısız).
     * @param plainPassword Düz (hashlenmemiş) parola.
     * @return Başarılıysa dolu User, başarısızsa null.
     */
    public User login(String username, String plainPassword) {
        if (username == null || plainPassword == null) {
            return null;
        }

        username = username.trim();
        if (username.isEmpty()) {
            return null;
        }

        // 1) Kullanıcıyı DB'den çek
        User user = userDAO.findByUsername(username);
        if (user == null) {
            // Böyle bir kullanıcı yok
            return null;
        }

        // 2) Şifre hash kontrolü
        String storedHash = user.getPasswordHash();
        if (storedHash == null || storedHash.isEmpty()) {
            return null;
        }

        boolean matches = HashUtil.verify(plainPassword, storedHash);
        if (!matches) {
            return null;
        }

        return user;
    }
}
