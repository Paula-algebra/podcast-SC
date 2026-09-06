package hr.algebra.podcast.config;

import hr.algebra.podcast.entity.Episode;
import hr.algebra.podcast.entity.User;
import hr.algebra.podcast.enums.ListeningContext;
import hr.algebra.podcast.enums.ListeningStatus;
import hr.algebra.podcast.enums.PlaybackSpeed;
import hr.algebra.podcast.enums.PodcastCategory;
import hr.algebra.podcast.enums.Role;
import hr.algebra.podcast.repository.EpisodeRepository;
import hr.algebra.podcast.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final EpisodeRepository episodeRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String SPOTIFY = "Spotify";

    public DataInitializer(
        UserRepository userRepository,
        EpisodeRepository episodeRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.episodeRepository = episodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) return;

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@podcast.hr");
        admin.setPassword(passwordEncoder.encode(System.getenv("ADMIN_PASSWORD")));
        admin.setRole(Role.ADMIN);
        admin = userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@podcast.hr");
        user.setPassword(passwordEncoder.encode(System.getenv("USER_PASSWORD")));
        user.setRole(Role.USER);
        userRepository.save(user);

        createEpisode(new EpisodeData()
                .title("The Wellness Industrial Complex")
                .showName("Maintenance Phase")
                .hosts("Aubrey Gordon, Michael Hobbes")
                .guests(null)
                .network(SPOTIFY)
                .episodeNumber("S3E12")
                .seasonNumber(3)
                .category(PodcastCategory.HEALTH_WELLNESS)
                .status(ListeningStatus.FINISHED)
                .context(ListeningContext.COMMUTE)
                .speed(PlaybackSpeed.SPEED_1_25X)
                .duration(92)
                .listened(92)
                .rating(5)
                .contentQuality(10)
                .audioQuality(9)
                .hostChemistry(10)
                .rewatchValue(9)
                .explicit(false)
                .subscribed(true)
                .bookmarkedQuote(true)
                .recommend(true)
                .release(LocalDate.of(2025, 11, 4))
                .listenedDate(LocalDate.of(2025, 11, 5))
                .added(LocalDate.of(2025, 10, 28))
                .mood("Investigative, sharp, devastating in the best way")
                .topic("Wellness industry, weight loss myths, junk science")
                .quote("The wellness industry isn't selling you health, it's selling you the anxiety that you don't have it.")
                .takeaway("Health is a social construct, weight is not a measure of worth")
                .review("Aubrey and Michael are the only ones who debunk wellness grifters this thoroughly. Listened on my Tuesday commute, missed my stop because I was too invested."
                        + "Recommended to everyone. Their backlog is gold.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("The Joe Schmo Show Phenomenon")
                .showName("You're Wrong About")
                .hosts("Sarah Marshall")
                .guests("Blair Braverman")
                .network(SPOTIFY)
                .episodeNumber("EP 178")
                .seasonNumber(null)
                .category(PodcastCategory.SOCIETY_CULTURE)
                .status(ListeningStatus.FINISHED)
                .context(ListeningContext.HOUSEWORK)
                .speed(PlaybackSpeed.SPEED_1_5X)
                .duration(78)
                .listened(78)
                .rating(5)
                .contentQuality(9)
                .audioQuality(8)
                .hostChemistry(10)
                .rewatchValue(8)
                .explicit(false)
                .subscribed(true)
                .bookmarkedQuote(true)
                .recommend(true)
                .release(LocalDate.of(2025, 10, 22))
                .listenedDate(LocalDate.of(2025, 10, 25))
                .added(LocalDate.of(2025, 10, 20))
                .mood("Nostalgic deep-dive, cozy, hilarious analysis")
                .topic("Reality TV history, 2003 culture, prank shows")
                .quote("We treat the past like another country, but it's really just a place where we used to live.")
                .takeaway("Reality TV is more constructed than fiction")
                .review("Sarah Marshall makes EVERYTHING interesting. The Joe Schmo show analysis was genuinely revelatory. I love how she finds the humanity in trash TV."
                        + "Sarah's takes are always thoughtful. Subscribed for years.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("Annie Mac on Hosting Cool Girl Energy")
                .showName("Off Menu")
                .hosts("Ed Gamble, James Acaster")
                .guests("Annie Mac")
                .network("Plosive Productions")
                .episodeNumber("EP 287")
                .seasonNumber(null)
                .category(PodcastCategory.COMEDY)
                .status(ListeningStatus.FINISHED)
                .context(ListeningContext.WALKING)
                .speed(PlaybackSpeed.SPEED_1_0X)
                .duration(71)
                .listened(71)
                .rating(4)
                .contentQuality(8)
                .audioQuality(9)
                .hostChemistry(10)
                .rewatchValue(6)
                .explicit(true)
                .subscribed(true)
                .bookmarkedQuote(false)
                .recommend(true)
                .release(LocalDate.of(2025, 9, 15))
                .listenedDate(LocalDate.of(2025, 9, 16))
                .added(LocalDate.of(2025, 9, 14))
                .mood("Pure chaos, dream restaurant format, British wit")
                .topic("Food preferences, hospitality, comedy podcast format")
                .quote("Welcome to The Dream Restaurant.")
                .takeaway("The best comedy comes from genuine specificity")
                .review("James Acaster's absolute commitment to the bit is a national treasure. Annie Mac was a perfect guest. I listened during my evening walk and got weird looks for laughing alone."
                        + "Comfort listen. Their dynamic is unmatched.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("Episode 1: Cassie Bernall")
                .showName("Last Podcast on the Left")
                .hosts("Marcus Parks, Henry Zebrowski, Ed Larson")
                .guests(null)
                .network(SPOTIFY)
                .episodeNumber("EP 590")
                .seasonNumber(null)
                .category(PodcastCategory.TRUE_CRIME)
                .status(ListeningStatus.LISTENING)
                .context(ListeningContext.WORKOUT)
                .speed(PlaybackSpeed.SPEED_1_5X)
                .duration(134)
                .listened(67)
                .rating(null)
                .contentQuality(null)
                .audioQuality(null)
                .hostChemistry(null)
                .rewatchValue(null)
                .explicit(true)
                .subscribed(true)
                .bookmarkedQuote(false)
                .recommend(false)
                .release(LocalDate.of(2026, 5, 1))
                .listenedDate(null)
                .added(LocalDate.of(2026, 5, 2))
                .mood("Heavy material, well-researched, dark humor balance")
                .topic("Columbine, religious mythology, school shootings")
                .quote(null)
                .takeaway(null)
                .review("Half-way through. Their Columbine series is heavy. Listening during workouts to dilute the intensity.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("How To Stop Doomscrolling")
                .showName("Hidden Brain")
                .hosts("Shankar Vedantam")
                .guests("Dr. Anna Lembke")
                .network("Hidden Brain Media")
                .episodeNumber("EP 412")
                .seasonNumber(null)
                .category(PodcastCategory.SCIENCE)
                .status(ListeningStatus.FINISHED)
                .context(ListeningContext.SLEEP)
                .speed(PlaybackSpeed.SPEED_1_0X)
                .duration(58)
                .listened(58)
                .rating(5)
                .contentQuality(10)
                .audioQuality(10)
                .hostChemistry(9)
                .rewatchValue(8)
                .explicit(false)
                .subscribed(true)
                .bookmarkedQuote(true)
                .recommend(true)
                .release(LocalDate.of(2025, 12, 3))
                .listenedDate(LocalDate.of(2025, 12, 7))
                .added(LocalDate.of(2025, 12, 1))
                .mood("Soothing, science-backed, paradigm-shifting")
                .topic("Dopamine, addiction, screen time, behavioral psychology")
                .quote("The relentless pursuit of pleasure leads to pain. The deliberate pursuit of pain leads to pleasure.")
                .takeaway("Dopamine fasting actually works, your phone is the problem")
                .review("Anna Lembke's book Dopamine Nation rewired my brain and this episode was a perfect intro. Shankar's voice is the most calming thing in audio."
                        + "Listen before bed. Deleted TikTok for a week after this.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("The Founder's Mode Phenomenon")
                .showName("Acquired")
                .hosts("Ben Gilbert, David Rosenthal")
                .guests("Brian Chesky")
                .network("Acquired LLC")
                .episodeNumber("S15E4")
                .seasonNumber(15)
                .category(PodcastCategory.BUSINESS)
                .status(ListeningStatus.SAVED_FOR_LATER)
                .context(null)
                .speed(null)
                .duration(245)
                .listened(0)
                .rating(null)
                .contentQuality(null)
                .audioQuality(null)
                .hostChemistry(null)
                .rewatchValue(null)
                .explicit(false)
                .subscribed(true)
                .bookmarkedQuote(false)
                .recommend(false)
                .release(LocalDate.of(2026, 3, 20))
                .listenedDate(null)
                .added(LocalDate.of(2026, 4, 1))
                .mood(null)
                .topic("Startup management, founder vs manager mode, Airbnb")
                .quote(null)
                .takeaway(null)
                .review("Saved for a long flight. 4+ hour episodes are an Acquired signature. Brian Chesky has been everywhere this year.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("Anyone Can Be A Stoic")
                .showName("Huberman Lab")
                .hosts("Andrew Huberman")
                .guests("Ryan Holiday")
                .network("Scicomm Media")
                .episodeNumber("EP 195")
                .seasonNumber(null)
                .category(PodcastCategory.HEALTH_WELLNESS)
                .status(ListeningStatus.FINISHED)
                .context(ListeningContext.WORKOUT)
                .speed(PlaybackSpeed.SPEED_2_0X)
                .duration(178)
                .listened(178)
                .rating(3)
                .contentQuality(7)
                .audioQuality(9)
                .hostChemistry(7)
                .rewatchValue(5)
                .explicit(false)
                .subscribed(false)
                .bookmarkedQuote(false)
                .recommend(false)
                .release(LocalDate.of(2025, 7, 14))
                .listenedDate(LocalDate.of(2025, 7, 18))
                .added(LocalDate.of(2025, 7, 12))
                .mood("Bro philosophy, dense, occasionally insightful")
                .topic("Stoicism, ancient philosophy, modern life")
                .quote("You don't rise to the level of your goals, you fall to the level of your systems.")
                .takeaway("Stoicism without the marketing is just discipline")
                .review("Listened at 2x because Huberman's pacing is glacial. Ryan Holiday has good points but the bro-coded delivery is exhausting. 3 stars max."
                        + "Not subscribing. One was enough.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("Episode 1: We're All Going to the Olympics")
                .showName("Normal Gossip")
                .hosts("Kelsey McKinney")
                .guests("Connor Franta")
                .network("Defector Media")
                .episodeNumber("S5E1")
                .seasonNumber(5)
                .category(PodcastCategory.STORYTELLING)
                .status(ListeningStatus.FINISHED)
                .context(ListeningContext.COOKING)
                .speed(PlaybackSpeed.SPEED_1_25X)
                .duration(62)
                .listened(62)
                .rating(5)
                .contentQuality(10)
                .audioQuality(9)
                .hostChemistry(10)
                .rewatchValue(9)
                .explicit(false)
                .subscribed(true)
                .bookmarkedQuote(true)
                .recommend(true)
                .release(LocalDate.of(2026, 1, 8))
                .listenedDate(LocalDate.of(2026, 1, 10))
                .added(LocalDate.of(2026, 1, 5))
                .mood("Anonymous gossip, sapphic chaos, perfect format")
                .topic("Gossip culture, relationship drama, anonymous stories")
                .quote("Friend, this story has so many twists I am going to need to lie down.")
                .takeaway("The best stories are real stories from regular people")
                .review("Kelsey McKinney's gasps are the soundtrack of my life. The Olympics story was UNHINGED in the best way. I literally screamed at my pasta sauce."
                        + "Comfort listen. Sundays are for Normal Gossip and dinner prep.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("How Tinder Killed The Meet Cute")
                .showName("Search Engine")
                .hosts("PJ Vogt")
                .guests(null)
                .network("Odyssey")
                .episodeNumber("EP 67")
                .seasonNumber(null)
                .category(PodcastCategory.TECHNOLOGY)
                .status(ListeningStatus.QUEUED)
                .context(null)
                .speed(null)
                .duration(54)
                .listened(0)
                .rating(null)
                .contentQuality(null)
                .audioQuality(null)
                .hostChemistry(null)
                .rewatchValue(null)
                .explicit(false)
                .subscribed(false)
                .bookmarkedQuote(false)
                .recommend(false)
                .release(LocalDate.of(2026, 4, 28))
                .listenedDate(null)
                .added(LocalDate.of(2026, 5, 1))
                .mood(null)
                .topic("Dating apps, modern romance, tech criticism")
                .quote(null)
                .takeaway(null)
                .review("PJ Vogt's voice can read me the phone book. In my queue for tomorrow's commute.")
                .addedBy(admin)
        );

        createEpisode(new EpisodeData()
                .title("Lex Speaks With Jeff Bezos")
                .showName("Lex Fridman Podcast")
                .hosts("Lex Fridman")
                .guests("Jeff Bezos")
                .network("Lex Fridman Productions")
                .episodeNumber("EP 405")
                .seasonNumber(null)
                .category(PodcastCategory.INTERVIEW)
                .status(ListeningStatus.SKIPPED)
                .context(ListeningContext.BACKGROUND)
                .speed(PlaybackSpeed.SPEED_1_75X)
                .duration(132)
                .listened(28)
                .rating(2)
                .contentQuality(5)
                .audioQuality(8)
                .hostChemistry(3)
                .rewatchValue(2)
                .explicit(false)
                .subscribed(false)
                .bookmarkedQuote(false)
                .recommend(false)
                .release(LocalDate.of(2024, 12, 14))
                .listenedDate(LocalDate.of(2024, 12, 20))
                .added(LocalDate.of(2024, 12, 14))
                .mood("Long-winded, hagiographic, surface-level")
                .topic("Amazon, space exploration, productivity")
                .quote(null)
                .takeaway("Lex needs to push back more on his guests")
                .review("Skipped at 28 minutes. Lex's interviews have become uncritical CEO infomercials. The whole tech-bro philosophical performance gets old fast."
                        + "Probably done with this show. Used to be good circa episode 100.")
                .addedBy(admin)
        );
    }

    private static class EpisodeData {
        private String title;
        private String showName;
        private String hosts;
        private String guests;
        private String network;
        private String episodeNumber;
        private Integer seasonNumber;
        private PodcastCategory category;
        private ListeningStatus status;
        private ListeningContext context;
        private PlaybackSpeed speed;
        private Integer duration;
        private Integer listened;
        private Integer rating;
        private Integer contentQuality;
        private Integer audioQuality;
        private Integer hostChemistry;
        private Integer rewatchValue;
        private boolean explicit;
        private boolean subscribed;
        private boolean bookmarkedQuote;
        private boolean recommend;
        private LocalDate release;
        private LocalDate listenedDate;
        private LocalDate added;
        private String mood;
        private String topic;
        private String quote;
        private String takeaway;
        private String review;
        private User addedBy;

        public EpisodeData title(String value) {
            this.title = value;
            return this;
        }
        public EpisodeData showName(String value) {
            this.showName = value;
            return this;
        }
        public EpisodeData hosts(String value) {
            this.hosts = value;
            return this;
        }
        public EpisodeData guests(String value) {
            this.guests = value;
            return this;
        }

        public EpisodeData network(String value) {
            this.network = value;
            return this;
        }
        public EpisodeData episodeNumber(String value) {
            this.episodeNumber = value;
            return this;
        }
        public EpisodeData seasonNumber(Integer value) {
            this.seasonNumber = value;
            return this;
        }
        public EpisodeData category(PodcastCategory value) {
            this.category = value;
            return this;
        }
        public EpisodeData status(ListeningStatus value) {
            this.status = value;
            return this;
        }
        public EpisodeData context(ListeningContext value) {
            this.context = value;
            return this;
        }

        public EpisodeData speed(PlaybackSpeed value) {
            this.speed = value;
            return this;
        }
        public EpisodeData duration(Integer value) {
            this.duration = value;
            return this;
        }
        public EpisodeData listened(Integer value) {
            this.listened = value;
            return this;
        }
        public EpisodeData rating(Integer value) {
            this.rating = value;
            return this;
        }
        public EpisodeData contentQuality(Integer value) {
            this.contentQuality = value;
            return this;
        }
        public EpisodeData audioQuality(Integer value) {
            this.audioQuality = value;
            return this;
        }
        public EpisodeData hostChemistry(Integer value) {
            this.hostChemistry = value;
            return this;
        }

        public EpisodeData rewatchValue(Integer value) {
            this.rewatchValue = value;
            return this;
        }
        public EpisodeData explicit(boolean value) {
            this.explicit = value;
            return this;
        }
        public EpisodeData subscribed(boolean value) {
            this.subscribed = value;
            return this;
        }
        public EpisodeData bookmarkedQuote(boolean value) {
            this.bookmarkedQuote = value;
            return this;
        }
        public EpisodeData recommend(boolean value) {
            this.recommend = value;
            return this;
        }
        public EpisodeData release(LocalDate value) {
            this.release = value;
            return this;
        }
        public EpisodeData listenedDate(LocalDate value) {
            this.listenedDate = value;
            return this;
        }
        public EpisodeData added(LocalDate value) {
            this.added = value;
            return this;
        }
        public EpisodeData mood(String value) {
            this.mood = value;
            return this;
        }
        public EpisodeData topic(String value) {
            this.topic = value;
            return this;
        }
        public EpisodeData quote(String value) {
            this.quote = value;
            return this;
        }
        public EpisodeData takeaway(String value) {
            this.takeaway = value;
            return this;
        }
        public EpisodeData review(String value) {
            this.review = value;
            return this;
        }
        public EpisodeData addedBy(User value) {
            this.addedBy = value;
            return this;
        }
    }

    private void createEpisode(EpisodeData data) {
        Episode e = new Episode();

        e.setTitle(data.title);
        e.setShowName(data.showName);
        e.setHosts(data.hosts);
        e.setGuests(data.guests);
        e.setNetwork(data.network);
        e.setEpisodeNumber(data.episodeNumber);
        e.setSeasonNumber(data.seasonNumber);
        e.setCategory(data.category);
        e.setStatus(data.status);
        e.setListeningContext(data.context);
        e.setPlaybackSpeed(data.speed);
        e.setDurationMinutes(data.duration);
        e.setMinutesListened(data.listened);
        e.setRating(data.rating);
        e.setContentQuality(data.contentQuality);
        e.setAudioQuality(data.audioQuality);
        e.setHostChemistry(data.hostChemistry);
        e.setRewatchValue(data.rewatchValue);
        e.setExplicitContent(data.explicit);
        e.setSubscribed(data.subscribed);
        e.setBookmarkedQuote(data.bookmarkedQuote);
        e.setRecommendToFriend(data.recommend);
        e.setReleaseDate(data.release);
        e.setListenedDate(data.listenedDate);
        e.setAddedDate(data.added);
        e.setMoodTags(data.mood);
        e.setMainTopic(data.topic);
        e.setMemorableQuote(data.quote);
        e.setKeyTakeaway(data.takeaway);
        e.setReview(data.review);
        e.setAddedBy(data.addedBy);

        episodeRepository.save(e);
    }
}
