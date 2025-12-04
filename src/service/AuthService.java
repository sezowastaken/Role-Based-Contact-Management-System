package service;

import model.User;
import model.Tester;
import model.JuniorDeveloper;
import model.SeniorDeveloper;
import model.Manager;

import java.time.LocalDateTime;

/**
 * DEMO login servisi.
 * - Şu an için veritabanı yok.
 * - Sabit demo kullanıcılar dönüyor.
 * - Yanlış kullanıcı adı/şifre için null döner.
 *
 * Kullanılabilir demo hesaplar:
 * tester / tester → Tester
 * junior / junior → JuniorDeveloper
 * senior / senior → SeniorDeveloper
 * manager / manager → Manager
 */
public class AuthService {

    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String u = username.trim().toLowerCase();
        String p = password.trim().toLowerCase();

        // Şimdilik hash kontrolü yapmıyoruz, doğrudan demo için plain-text
        // eşleştiriyoruz.
        // User sınıfındaki "passwordHash" alanına da şimdilik demo bir değer koyuyoruz.

        LocalDateTime now = LocalDateTime.now();

        // Tester demo kullanıcısı
        if (u.equals("tester") && p.equals("tester")) {
            return new Tester(
                    1, // id
                    "tester", // username
                    "123", // passwordHash (gerçekte hash olacak)
                    "Demo", // name
                    "Tester", // surname
                    now // createdAt
            );
        }

        // Junior Dev demo kullanıcısı
        if (u.equals("junior") && p.equals("junior")) {
            return new JuniorDeveloper(
                    2,
                    "junior",
                    "demoHashJunior",
                    "Demo",
                    "Junior",
                    now);
        }

        // Senior Dev demo kullanıcısı
        if (u.equals("senior") && p.equals("senior")) {
            return new SeniorDeveloper(
                    3,
                    "senior",
                    "demoHashSenior",
                    "Demo",
                    "Senior",
                    now);
        }

        // Manager demo kullanıcısı
        if (u.equals("manager") && p.equals("manager")) {
            return new Manager(
                    4,
                    "manager",
                    "demoHashManager",
                    "Demo",
                    "Manager",
                    now);
        }

        // Hiçbiri uymadıysa login başarısız
        return null;
    }
}
