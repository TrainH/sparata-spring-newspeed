package spartaspringnewspeed.spartafacespeed.liking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spartaspringnewspeed.spartafacespeed.liking.repository.LikingPostRepository;

@Service
@RequiredArgsConstructor
public class LikingPostService {
    private final LikingPostRepository likingPostRepository;
}
