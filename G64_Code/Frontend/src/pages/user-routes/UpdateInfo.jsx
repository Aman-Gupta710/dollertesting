import React, { useEffect, useState } from "react";
import Base from "../../components/Base";
import { getCurrentUserDetail, isLoggedIn } from "../../auth"
import { Button } from 'reactstrap'
import { updateUser } from "../../services/user-service";
import { useNavigate } from "react-router-dom";

const UpdateInfo = () => {
    const navigate=useNavigate()
    const [currentUser, setCurrentUser] = useState(JSON.parse(localStorage.getItem("data")) || null)
    const [login, setLogin] = useState(false)
    const [userState,setUserState]=useState({...currentUser,name:currentUser.name,about:currentUser.about})

    useEffect(() => {
        setCurrentUser(getCurrentUserDetail())
        setLogin(isLoggedIn())
    }, [])

    const handleChange = (e) => {
        const {name,value}=e.target
        setCurrentUser(prevState => ({
            ...prevState,
            [name]:value
        }))
    }

    const handleSubmit=()=>{
        console.log(currentUser)
        updateUser(currentUser,currentUser.id).then((data)=>{
            setCurrentUser(data)
            localStorage.setItem("data", JSON.stringify(data));
            navigate(`/user/profile-info/${data.id}`)
        })
    }

    return (
        <Base>
            <div className="container bootstrap snippets bootdey" style={{ marginTop: '20px' }}>
                <h1 className="text-primary">Edit Profile</h1>
                <hr />
                <div className="row">
                    <div className="col-md-3">
                        <div className="text-center">
                            <img src="https://cdn.dribbble.com/users/6142/screenshots/5679189/media/1b96ad1f07feee81fa83c877a1e350ce.png?compress=1&resize=400x300&vertical=top"
                                className="avatar img-circle img-thumbnail"
                                alt="avatar" />
                        </div>
                    </div>

                    <div className="col-md-9 personal-info">
                        <h3>Personal info</h3>

                        <form className="form-horizontal" role="form">
                            <div className="form-group" style={{ marginBottom: "10px" }}>
                                <label className="col-lg-3 control-label">Name:</label>
                                <div className="col-lg-8">
                                    <input
                                        className="form-control" type="text"
                                        name="name" value={currentUser.name}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>
                            <div className="form-group" style={{ marginBottom: "10px" }}>
                                <label className="col-lg-3 control-label">About</label>
                                <div className="col-lg-8">
                                    <textarea className="form-control"
                                        name="about" type="text" value={currentUser.about}
                                        onChange={handleChange}
                                    />
                                </div>
                            </div>

                            <div className="form-group" style={{ marginBottom: "10px" }}>
                                <label className="col-lg-3 control-label">Email:</label>
                                <div className="col-lg-8">
                                    <input className="form-control" type="text" value={currentUser.email} disabled />
                                </div>
                            </div>
                            <div>
                            <Button color="warning" style={{ marginTop: "5px" }} onClick={handleSubmit}>Update Profile</Button>
                            
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </Base>
    )
}

export default UpdateInfo