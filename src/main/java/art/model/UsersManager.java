package art.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import art.repository.UsersRepository;

@Service
public class UsersManager {
    @Autowired
    private UsersRepository userRepository;

    // ✅ User Signup (stores plain text password)
    public String addUser(Users user) {    
        if (userRepository.existsById(user.getEmail())) {
            return "401::Email already exists";    
        }
        
        user.setPassword(user.getPassword()); // store as plain text
        userRepository.save(user);
        return "200::User Registered Successfully";
    }

    // ✅ Password Recovery (still generic, no direct exposure)
    public String recoverPassword(String email) {
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return String.format("Dear %s, please follow the password reset process.", 
                                 optionalUser.get().getFullname());
        }
        return "404::Email not found";
    }

    // ✅ Validate Credentials (direct string comparison)
    public boolean validateCredentials(String email, String password) {
        Optional<Users> optionalUser = userRepository.findByEmail(email);
        return optionalUser.isPresent() && 
               password.equals(optionalUser.get().getPassword());
    }

    // ✅ Get Full Name
    public String getFullname(String email) {
        return userRepository.findByEmail(email)
                             .map(Users::getFullname)
                             .orElse("404::User not found");
    }
}
