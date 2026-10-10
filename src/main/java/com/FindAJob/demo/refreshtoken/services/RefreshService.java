package com.FindAJob.demo.refreshtoken.services;

import com.FindAJob.demo.SecurityPackage.AuthResDTO;
import com.FindAJob.demo.SecurityPackage.JWTService;
import com.FindAJob.demo.companies.domain.entities.Companies;
import com.FindAJob.demo.companies.domain.repos.CompRepository;
import com.FindAJob.demo.refreshtoken.domain.repos.RefreshRepository;
import com.FindAJob.demo.refreshtoken.requests.RefreshReqDTO;
import com.FindAJob.demo.refreshtoken.response.RefreshResDTO;
import com.FindAJob.demo.refreshtoken.domain.entities.RefreshToken;
import com.FindAJob.demo.reg_users.domain.entities.Reg_Users;
import com.FindAJob.demo.reg_users.domain.repos.Reg_UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshService {

    private final RefreshRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwt;
    private final Reg_UsersRepository userRepo;
    private final CompRepository compRepo;


    public RefreshService(RefreshRepository repo,
                          PasswordEncoder passwordEncoder,
                          JWTService jwt,
                          Reg_UsersRepository userRepo, CompRepository compRepo) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwt = jwt;
        this.userRepo = userRepo;
        this.compRepo = compRepo;
    }
////////////////////////////////////////////////////////////////////
/// ///////////////////////////////////////////////////////////////
/// ////////// Reg_Users Refresh Service //////////////////////////
/// ////////////////////////////////////////////////////////////////
/// ///////////////////////////////////////////////////////////////
///
///
    public String createRefreshT(Reg_Users user){



        String token = getRefToken();

            RefreshToken refreshT = new RefreshToken(passwordEncoder.encode(token),
                    Instant.now(),
                    Instant.now().plus(Duration.ofDays(1/2)),
                    user);

                repo.save(refreshT);

      //  RefreshResDTO refRes = RefreshResDTO.from(refreshT, user);

        return token;

    }

    public String checkForExistingRefToken(Reg_Users user){

        //find user existing tokens
        Optional<RefreshToken> rToken = repo.findByUserEmail(user.getEmail());

        String refT= "";

        //check how many user has
        long num = checkUserTokens(user.getEmail());


        rToken.ifPresent(repo::delete);

                refT = createRefreshT(user);

                return refT;

    }

    public RefreshResDTO refreshAccessToken(RefreshReqDTO request) {

        Optional<RefreshToken> rToken = Optional.of(repo.findByUserEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Refresh Token not found!" + request.token())));
        //String hashedToken = passwordEncoder.encode(request.token());

        if(passwordEncoder.matches(request.token(), rToken.get().getToken())) {

            String userEmail = rToken.get().getUser().getEmail();

            Reg_Users user = rToken.get().getUser();

            long num = this.checkUserTokens(userEmail);

            String accToken = "";

            //    repo.deleteAllByUserEmail(userEmail);

            if (validateRefToken(rToken.get())) {

                accToken = jwt.generateToken(userEmail,
                        rToken.get().getUser().getName(),
                        rToken.get().getUser().getRole());

                return new RefreshResDTO(userEmail,
                        accToken,
                        rToken.get().getToken());

            } else {

                repo.delete(rToken.get());

                String token = getRefToken();

                RefreshToken rToken2 = new RefreshToken(token,
                        Instant.now().plus(Duration.ofDays(1 / 2)),
                        Instant.now(),
                        user);

                repo.save(rToken2);

                accToken = jwt.generateToken(userEmail,
                        rToken2.getUser().getName(),
                        rToken2.getUser().getRole());

                return new RefreshResDTO(userEmail,
                        accToken,
                        rToken2.getToken());

            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Refresh Token");}

    }


    public boolean validateRefToken(RefreshToken rToken){

        RefreshToken refToken = repo.getReferenceById(rToken.getId());

        //this approach checks if the expiration is
        // after the current time then returns a true else a false
              if(Instant.now().isAfter(refToken.getExpiresAt())){
                  return true;
              } else {
                  return false;
              }



        //  if(rToken.getExpiresAt() >= Instant.now()) this didn't work
        //Instant time1 = Instant.now()

    }

    public AuthResDTO revokeOrSuspendRefToken(String email){
        Optional<RefreshToken> refT = Optional.of(repo.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Refresh token doesn't exist")));

            RefreshToken rT = refT.get();

                repo.deleteById(rT.getId());

                    return new AuthResDTO(
                            rT.getUser().getName(),
                            rT.getUser().getEmail(),
                            "Unauthorized"
                    );

    }

    //replace this with access token cancellation instead bcs they'll still be able to access the platform while it isn't expired

////////////////   Functions used in the service      //////////////////

public String getRefToken(){
    SecureRandom random = new SecureRandom();//declaring a "Secure random" variable which allows u to use
    // methods for generating and managing a random value in bytes

        byte[] bytes = new byte[32]; //declaration of an array of
        //bytes, 32, to be precise as specified in the square brackets

            random.nextBytes(bytes);// generates random of number
            // of bytes equivalent to the array

                String token = Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(bytes);

                    return token;
}

    public Long checkUserTokens(String email) {

        List<RefreshToken> refTokens = repo.findAllByUserEmail(email);

        long num = 0L;

        for (int i = 0; i < refTokens.size(); i++) {
            if (refTokens.get(i) != null) {
                num += 1;
            }
        }

        return num;
    }


    ///////////////////////////////////////////////////////////
    ////////////////// Companu Refresh Service////////////////////



    public String createCompRefToken(Companies company){

        String token = getRefToken();

        RefreshToken refT = new RefreshToken(passwordEncoder.encode(token),
                                                Instant.now().plus(Duration.ofDays(1/2)),
                                                  Instant.now(),
                                                company);

        repo.save(refT);

        return token;
    }

    public String checkForExistingCompRefToken(Companies comp){

        //find user existing tokens
        Optional<RefreshToken> rToken = repo.findByComp_CompEmail(comp.getCompEmail());

        String refT= "";

      //check how many rTokens user has
        long num = checkUserTokens(comp.getCompEmail());

        rToken.ifPresent(repo::delete);
            refT = createCompRefToken(comp);

            return refT;
    }


    public RefreshResDTO refreshCompAccToken(RefreshReqDTO request){

        Optional<RefreshToken> refToken = Optional.of(repo.findByComp_CompEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Refresh Token not found")));

        if(passwordEncoder.matches(request.token(), refToken.get().getToken())) {

            RefreshToken rToken = refToken.get();

            String accToken = "";

            Companies comp = rToken.getComp();

            if (validateRefToken(rToken)) {
                accToken = jwt.generateToken(rToken.getComp().getCompEmail(),
                        rToken.getComp().getComp_name(),
                        rToken.getComp().getRole());

                return new RefreshResDTO(rToken.getComp().getCompEmail(),
                        accToken,
                        rToken.getToken());
            } else {

                repo.delete(rToken);

                String token = getRefToken();

                RefreshToken rToken2 = new RefreshToken(token,
                        Instant.now().plus(Duration.ofDays(1 / 2)),
                        Instant.now(),
                        comp);

                accToken = jwt.generateToken(rToken2.getComp().getCompEmail(),
                        rToken2.getComp().getComp_name(),
                        rToken2.getComp().getRole());

                repo.save(rToken2);

                return new RefreshResDTO(comp.getCompEmail(),
                        accToken,
                        rToken2.getToken());
            }
        } else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid Refresh Token");
        }
    }

}
