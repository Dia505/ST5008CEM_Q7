import "../css-files/ProfilePage.css"
import Header from "./Header.tsx";
import {faThumbsUp, faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useMutation, useQuery} from "react-query";
import axios from "axios";
import {useEffect, useState} from "react";
import {useForm} from "react-hook-form";
function ProfilePage() {
    const userId = localStorage.getItem("loggedInUserId");
    const [selectedImage, setSelectedImage] = useState(null);
    const {register, handleSubmit, setValue} = useForm();
    const [isAddFormVisible, setAddFormVisible] = useState(false);
    const [userPosts, setUserPosts] = useState([]);
    const [postLike, setPostLike] = useState(0);

    const {data} = useQuery(
        ["GETUSERDATA", userId],
        async () => {
            const response = await axios.get(`http://localhost:8080/user/get-user-by-id/${userId}`);
            return response.data;
        }
    );

    const fetchUserPosts = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/post/get-post-by-userId/${userId}`);
            setUserPosts(response.data);
        }
        catch(error) {
            console.error("Error fetching user's posts: ",error);
        }
    }

    useEffect(() => {
        fetchUserPosts()
    }, []);

    const uploadPost = useMutation({
        mutationKey: "UPLOAD_POST",
        mutationFn: async (requestData: any) => {
            try {
                const formData = new FormData();
                formData.append("postImage", requestData.image[0]);
                formData.append("title", requestData.title);
                formData.append("hashtag", requestData.hashtag);
                formData.append("userId", userId);

                const response = await axios.post("http://localhost:8080/post/upload-post", formData, {
                    headers: {
                        "Content-Type": "multipart/form-data"
                    }
                });

                console.log(response);
                return response.data;
            }
            catch (error) {
                console.log("Error uploading file: ",error);
            }
        },
        onSuccess: () => {
            setAddFormVisible(false);
            alert("The post has been uploaded!");
        },
        onMutate: (requestData) => {
            return { userId };
        }
    })

    const clearAddForm = () => {
        setValue("title", "");
        setValue("hashtag", "");
    }

    const onSubmitUpload = (formData: any): void => {
        uploadPost.mutate(formData);
        clearAddForm();
    }

    return (
        <>
            <Header/>

            <div className={"centre-profile"}>
                <div className={"title-addPost-container-profile"}>
                    <p className={"profile-title-text"}>Profile</p>
                    <button className={"add-post-btn"} onClick={() => {
                        setAddFormVisible(!isAddFormVisible);
                    }}>Add Post</button>
                </div>

                <div className={"user-details-container"}>
                    <FontAwesomeIcon icon={faUser} className={"profile-icon"}/>

                    {data && (
                        <div className={"user-name-address-container"}>
                            <p>Name: {data.fullName}</p>
                            <p className={"profile-address-text"}>Address: {data.address}</p>
                        </div>
                    )}
                </div>

                {isAddFormVisible && (
                    <form onSubmit={handleSubmit(onSubmitUpload)}>
                        <div className={"add-post-form"}>
                            <p>Create Post</p>

                            <div className={"image-main-container"}>
                                <label className={"image-upload-label"} htmlFor={"productImageId"}>
                                    <div className={"image-upload-container"}>
                                        {selectedImage ? (
                                            <img
                                                className={"addForm-image"}
                                                src={selectedImage}
                                                alt="Selected Image"
                                            />
                                        ) : (
                                            <p className={"add-image-text"}>ADD IMAGE</p>
                                        )}
                                    </div>
                                </label>
                                <input id={"productImageId"}
                                       type={"file"}
                                       className={"product-image-input"}
                                       {...register("image", {
                                           onChange: (e) => {
                                               const file = e.target.files[0];
                                               if(file) {
                                                   const imageUrl = URL.createObjectURL(file);
                                                   setSelectedImage(imageUrl);
                                               }
                                           }
                                       })}/>
                            </div>

                            <input placeholder={"Title"} {...register("title")}/>
                            <input placeholder={"Hashtag"} {...register("hashtag")}/>

                            <button className={"upload-btn"} type={"submit"}>Upload</button>
                        </div>
                    </form>
                )}

                <div className={"profile-post-list"}>
                    {userPosts.map((post) => (
                        <div className={"profile-post-container"} key={post.postId}>
                            <div className={"profile-post-user"}>
                                <FontAwesomeIcon icon={faUser} className={"profile-icon"}/>

                                {data && (
                                    <div className={"user-name-container"}>
                                        <p>{data.fullName}</p>
                                    </div>
                                )}
                            </div>
                            <div className={"profile-post-details"}>
                                <p>{post.title}</p>
                                <p className={"profile-post-hashtag"}>{post.hashtag}</p>
                                <img className={"profile-post"} src={`/${post.postImage}`}/>
                                <div className={"like-container"}>
                                    <FontAwesomeIcon icon={faThumbsUp} />
                                    <p>Like</p>
                                    <p>{postLike}</p>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    )
}
export default ProfilePage;