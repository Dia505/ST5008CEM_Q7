import "../css-files/ProfilePage.css"
import Header from "./Header.tsx";
import {faUser} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {useQuery} from "react-query";
import axios from "axios";
import {useState} from "react";
import {useForm} from "react-hook-form";
function ProfilePage() {
    const userId = localStorage.getItem("loggedInUserId");
    const [selectedImage, setSelectedImage] = useState(null);
    const {register, handleSubmit} = useForm();
    const [isAddFormVisible, setAddFormVisible] = useState(false);

    const {data} = useQuery(
        ["GETUSERDATA", userId],
        async () => {
            const response = await axios.get(`http://localhost:8080/user/get-user-by-id/${userId}`);
            return response.data;
        }
    );

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
                    <form>
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

                            <input placeholder={"Title"}/>
                            <input placeholder={"Hashtag"}/>
                        </div>
                    </form>
                )}
            </div>
        </>
    )
}
export default ProfilePage;