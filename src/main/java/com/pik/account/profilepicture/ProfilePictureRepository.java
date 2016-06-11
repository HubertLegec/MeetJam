package com.pik.account.profilepicture;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfilePictureRepository extends MongoRepository<ProfilePicture, String> {
    ProfilePicture getProfilePictureByUserLogin(String userLogin);
}
