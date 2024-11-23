package myaong.popolog.blogservice.service;

import myaong.popolog.blogservice.entity.Profile;

public interface FollowQueryService {

	Boolean existsByMembers(Profile requester, Profile profile);
}
