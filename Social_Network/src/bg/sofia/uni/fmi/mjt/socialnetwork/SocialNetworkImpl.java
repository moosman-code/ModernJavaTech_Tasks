package bg.sofia.uni.fmi.mjt.socialnetwork;

import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.*;

public class SocialNetworkImpl implements SocialNetwork{

    private Set<UserProfile> users;
    private ArrayList<SocialFeedPost> posts;

    public SocialNetworkImpl() {
        this.users = new HashSet<>();
    }

    @Override
    public void registerUser(UserProfile userProfile) throws UserRegistrationException {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        try {
            if (users.contains(userProfile)) {
                throw new UserRegistrationException();
            }
            users.add(userProfile);
        }
        catch (UserRegistrationException e) {
            throw e;
        }
    }

    @Override
    public Set<UserProfile> getAllUsers() {
        return Set.copyOf(users);
    }

    @Override
    public Post post(UserProfile userProfile, String content) throws UserRegistrationException {
        if (userProfile == null || content == null) {
            throw new IllegalArgumentException("User profile and post content cannot be null");
        }

        try {
            if (!users.contains(userProfile)) {
                throw new UserRegistrationException();
            }

            SocialFeedPost post = new SocialFeedPost(userProfile, content);
            posts.add(post);
            return post;
        }
        catch (UserRegistrationException e) {
            throw e;
        }
    }

    @Override
    public Collection<Post> getPosts() {
        return List.copyOf(posts);
    }

    @Override
    public Set<UserProfile> getReachedUsers(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Post cannot be null");
        }

        Set<UserProfile> result = new HashSet<>();
        UserProfile author = post.getAuthor();
        Collection<UserProfile> authorFriends = author.getFriends();

        for (UserProfile user : users) {
            if (haveTheSameFriendNetwork(author, user) && haveCommonInterests(author, user)) {
                result.add(user);
            }
        }

        return result;
    }

    private boolean haveCommonInterests(UserProfile first, UserProfile second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("User profiles cannot be null");
        }

        for (Interest interest : first.getInterests()) {
            if (second.getInterests().contains(interest)) {
                return true;
            }
        }

        return false;
    }

    private boolean haveTheSameFriendNetwork(UserProfile first, UserProfile second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("User profiles cannot be null");
        }

        Set<UserProfile> done = new HashSet<>();
        Queue<UserProfile> check = new ArrayDeque<>();
        check.add(first);

        while(!check.isEmpty()) {
            UserProfile current = check.poll();
            if (done.contains(current)) {
                continue;
            }

            if (current.equals(second)) {
                return true;
            }
            check.addAll(current.getFriends());
            done.add(current);
        }

        return false;
    }

    @Override
    public Set<UserProfile> getMutualFriends(UserProfile firstUser, UserProfile secondUser) throws UserRegistrationException {
        if (firstUser == null || secondUser == null) {
            throw new IllegalArgumentException("User profiles and post cannot be null");
        }

        try {
            if (!users.contains(firstUser) && !users.contains(secondUser)) {
                throw new UserRegistrationException();
            }

            Collection<UserProfile> intersection = new HashSet<>(firstUser.getFriends());
            intersection.retainAll(secondUser.getFriends());
            return Set.copyOf(intersection);
        }
        catch (UserRegistrationException e) {
            throw e;
        }
    }

    @Override
    public SortedSet<UserProfile> getAllProfilesSortedByFriendsCount() {
        SortedSet<UserProfile> sortedUsers = new TreeSet<>(users);
        return sortedUsers;
    }
}
