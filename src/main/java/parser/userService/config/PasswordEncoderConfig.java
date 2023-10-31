package parser.userService.config;

import com.password4j.BcryptFunction;
import com.password4j.types.Bcrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordEncoderConfig {
    @Bean
    public BcryptFunction bcryptFunction(){
        return BcryptFunction.getInstance(Bcrypt.B, 12);
    }

}
