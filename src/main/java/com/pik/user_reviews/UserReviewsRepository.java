package com.pik.user_reviews;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserReviewsRepository extends MongoRepository<Review,String>{
    List<Review> findByReviewedLogin(String reviewedLogin, Pageable pageable);
}
