package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.*;

public class DefaultUserProfile implements UserProfile, Comparable<UserProfile> {

    private final String username;
    private Set<Interest> interests;
    private Set<UserProfile> friends;

    public DefaultUserProfile(String username) {
        this.username = username;
        this.interests = new HashSet<>();
        this.friends = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfile)) return false;
        UserProfile otherUser = (UserProfile) o;
        return Objects.equals(username, otherUser.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public Collection<Interest> getInterests() {
        return List.copyOf(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }

        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        if (interest == null) {
            throw new IllegalArgumentException("Interest cannot be null");
        }

        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return List.copyOf(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Friend cannot be null");
        }

        boolean result = friends.add(userProfile);
        if (result) {
            userProfile.addFriend(this);
        }
        return result;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("Friend cannot be null");
        }

        boolean result = friends.remove(userProfile);
        if (!result) {
            userProfile.unfriend(this);
        }
        return result;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        return friends.contains(userProfile);
    }

    @Override
    public int compareTo(UserProfile other) {
        return this.friends.size() - other.getFriends().size();
    }
}
