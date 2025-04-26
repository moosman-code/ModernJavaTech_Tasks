package bg.sofia.uni.fmi.mjt.socialnetwork.post;

import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.time.LocalDateTime;
import java.util.*;

public class SocialFeedPost implements Post {

    private String id;
    private UserProfile author;
    private LocalDateTime publishedOn;
    private String content;
    private Map<UserProfile, ReactionType> reactions;

    public SocialFeedPost(UserProfile author, String content) {
        this.id = generateUID();
        this.author = author;
        this.publishedOn = LocalDateTime.now();
        this.content = content;
        this.reactions = new HashMap();
    }

    private String generateUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String getUniqueId() {
        return id;
    }

    @Override
    public UserProfile getAuthor() {
        return author;
    }

    @Override
    public LocalDateTime getPublishedOn() {
        return publishedOn;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public boolean addReaction(UserProfile userProfile, ReactionType reactionType) {
        if (userProfile == null || reactionType == null) {
            throw new IllegalArgumentException("User profile and reaction type cannot be null");
        }

        ReactionType oldReaction = reactions.put(userProfile, reactionType);
        return oldReaction == null;
    }

    @Override
    public boolean removeReaction(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("User profile cannot be null");
        }

        ReactionType oldReaction = reactions.remove(userProfile);
        return oldReaction != null;
    }

    // Write hashCode equals and Comparable for UserProfile
        @Override
        public Map<ReactionType, Set<UserProfile>> getAllReactions() {
            Map<ReactionType, Set<UserProfile>> result = new HashMap<>();

            for (Map.Entry<UserProfile, ReactionType> entry : reactions.entrySet()) {
                Set<UserProfile> reaction = result.putIfAbsent(entry.getValue(), Set.of(entry.getKey()));

                if (reaction != null) {
                    result.get(entry.getValue()).add(entry.getKey());
                }
            }

            // Make the sets containing the users unmodifiable
            Map<ReactionType, Set<UserProfile>> unmodifiableResult = new HashMap<>();
            for (Map.Entry<ReactionType, Set<UserProfile>> entry : result.entrySet()) {
                unmodifiableResult.put(entry.getKey(), Set.copyOf(entry.getValue()));
            }

            return Map.copyOf(unmodifiableResult);
        }

    @Override
    public int getReactionCount(ReactionType reactionType) {
        if (reactionType == null) {
            throw new IllegalArgumentException("Reaction type cannot be null");
        }

        int count = 0;
        for (ReactionType reaction : reactions.values()) {
            if (reactionType == reaction) {
                ++count;
            }
        }

        return count;
    }

    @Override
    public int totalReactionsCount() {
        return reactions.size();
    }
}
