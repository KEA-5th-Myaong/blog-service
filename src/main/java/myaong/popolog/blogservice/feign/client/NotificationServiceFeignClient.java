package myaong.popolog.blogservice.feign.client;

import myaong.popolog.blogservice.feign.dto.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

//TODO: url로 명시적으로 지정하지 않고도 디스커버리 로드 밸런싱을 이용해서 동작할 수 있도록 해야함
@FeignClient(name = "notification-service", url = "localhost:9085")
public interface NotificationServiceFeignClient {

    @PostMapping("/notifications/{type}")
    void sendNotification(@RequestBody NotificationRequest request, @PathVariable("type") String type, @RequestHeader("memberId") Long memberId);

}