import "../css-files/HomePage.css"
import Header from "./Header.tsx";
import axios from "axios";
import {useEffect, useState} from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faThumbsUp, faUser} from "@fortawesome/free-solid-svg-icons";
function HomePage() {
    const userId = localStorage.getItem("loggedInUserId");
    const [friendList, setFriendList] = useState([]);
    const [friendPosts, setFriendPosts] = useState([]);
    const [recommendedPosts, setRecommendedPosts] = useState([]);

    // To return friend list of user
    useEffect(() => {
        const returnFriendList = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/user/get-friend-list/${userId}`);
                setFriendList(response.data);
            } catch (error) {
                console.error("Error getting friend list:", error);
            }
        };

        returnFriendList();
        // Fetch friend list when userId changes
    }, [userId]);

    // To return posts of friends using the friend list
    useEffect(() => {
        const returnFriendPosts = async () => {
            try {
                // promises used to perform multiple asynchronous operations that fetch posts made by friends, concurrently
                // Iterate over each friend from friendList array and return data of posts made by each friend
                const promises = friendList.map(async (friend) => {
                    const response = await axios.get(`http://localhost:8080/post/get-post-by-userId/${friend.userId}`);
                    return response.data;
                });
                // friendPostsData holds an array containing resolved value of all promises in the promises array
                // i.e. contains posts made by each friend
                const friendPostsData = await Promise.all(promises);

                // prevents duplicate posts from being displayed based on postId
                // flattens friendPostsData meaning converts multi-dimensional array to single dimensional array
                // for easier filtering
                // filter function iterates over each post in flattened array and only retains posts with first occurrence
                const uniquePosts = friendPostsData.flat().filter((post, index, self) =>
                    index === self.findIndex((p) => p.postId === post.postId)
                );
                setFriendPosts(uniquePosts);
            } catch (error) {
                console.error("Error getting posts of friends:", error);
            }
        };

        returnFriendPosts();
    }, [friendList]);

    // For displaying recommended posts
    useEffect(() => {
        if (userId) {
            const fetchRecommendedPosts = async () => {
                try {
                    const response = await axios.get(`http://localhost:8080/recommend/recommendations/${userId}`);
                    setRecommendedPosts(response.data);
                } catch (error) {
                    console.error("Error getting recommended posts:", error);
                }
            };

            fetchRecommendedPosts();
        }
    }, [userId]);

    // If recommendPosts length is greatet than 0, meaning if it contains data, that array of data is displayed
    // Else, all posts of all friends is displayed
    const displayPosts = userId && friendList.length > 0 && friendPosts.length > 0 ?
        recommendedPosts.length > 0 ? recommendedPosts : friendPosts :
        [];

    // handles the likes gained by posts
    const handleLikePost = async (postId) => {
        try {
            await axios.post(`http://localhost:8080/post/like/${postId}`);
        }
        catch (error) {
            console.error("Error liking post:", error);
        }
    };

    // handles the likes provided by the logged-in user for a post
    const handleSaveLike = async (postId) => {
        try {
            await axios.post("http://localhost:8080/like/save-like", {
                postId: postId,
                userId: userId,
            });
            // Update UI or fetch updated post data
        } catch (error) {
            console.error("Error saving like:", error);
            // Handle error
        }
    };

    // handles both handleLikePost and handleSaveLike functions
    // to have both functions be executed by the same div button
    const handleLikeFunctions = (postId) => {
        handleLikePost(postId);
        handleSaveLike(postId);
        window.location.reload();
    }

    return (
        <>
            <Header/>
            <div className={"home-centre"}>
                <p className={"home-title-text"}>Home</p>

                {/*This div displays friend's posts*/}
                <div className={"home-post-list"}>
                    {displayPosts.map((post) => (
                        <div className={"home-post-container"} key={post.postId}>
                            <div className={"home-post-user"}>
                                <FontAwesomeIcon icon={faUser} className={"profile-icon"}/>
                                <div className={"user-name-container"}>
                                    <p>{post.user.fullName}</p>
                                </div>
                            </div>
                            <div className={"home-post-details"}>
                                <p>{post.title}</p>
                                <p className={"home-post-hashtag"}>{post.hashtag}</p>
                                <img className={"home-post"} src={`/${post.postImage}`}/>

                                {/*This div handles all like functionality*/}
                                <div className={"like-container"} onClick={() => handleLikeFunctions(post.postId)}>
                                    <FontAwesomeIcon icon={faThumbsUp}/>
                                    <p>Like</p>
                                    <p>{post.likes}</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}
export default HomePage;