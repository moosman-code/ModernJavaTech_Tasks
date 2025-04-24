import bg.sofia.uni.fmi.mjt.socialnetwork.SocialNetwork;
import bg.sofia.uni.fmi.mjt.socialnetwork.SocialNetworkImpl;
import bg.sofia.uni.fmi.mjt.socialnetwork.exception.UserRegistrationException;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.Post;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import static bg.sofia.uni.fmi.mjt.socialnetwork.profile.Interest.BOOKS;

public class Main {
    public static void main(String[] args) {
        // Create some test users
        UserProfile alice = new DefaultUserProfile("alice");
        UserProfile bob = new DefaultUserProfile("bob");
        UserProfile charlie = new DefaultUserProfile("charlie");
        UserProfile dad = new DefaultUserProfile("dad");

        alice.addFriend(bob);
        alice.addFriend(charlie);
        bob.addFriend(charlie);
        bob.addInterest(BOOKS);
        charlie.addInterest(BOOKS);
//        bob.getFriends().forEach(fr -> System.out.println(fr.getUsername()));

        // Create some test posts
        Post post1 = new SocialFeedPost(alice, "This is Alice's first post!");
        Post post2 = new SocialFeedPost(bob, "Bob just joined the platform!");
        Post post3 = new SocialFeedPost(charlie, "Charlie shares his thoughts on technology.");

        SocialNetwork network = new SocialNetworkImpl();
        try {
            network.registerUser(alice);
            network.registerUser(bob);
            network.registerUser(charlie);
            network.registerUser(dad);

//          network.getMutualFriends(alice, bob).forEach(fr -> System.out.println(fr.getUsername()));
//            network.getReachedUsers(post2).forEach(user -> System.out.println(user.getUsername()));
        } catch (UserRegistrationException e) {
            System.out.println("Error while registrating");
        }
    }
}