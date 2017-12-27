package pl.teneusz.dream.journal.web.rest;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.teneusz.dream.journal.repository.UserInfoRepository;
import pl.teneusz.dream.journal.service.dto.UserInfoDto;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserInfoResource {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping("/userInfo/{id}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable Long id){
        UserInfoDto dto = userInfoRepository.getUserInfoByUserId(id);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dto));
    }


}
