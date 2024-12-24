package spartaspringnewspeed.spartafacespeed.liking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spartaspringnewspeed.spartafacespeed.liking.repository.PostLikeRepository;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;

}
