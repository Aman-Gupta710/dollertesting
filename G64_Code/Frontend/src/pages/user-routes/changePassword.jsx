import React,{useState,useEffect} from 'react'
import Base from "../../components/Base";
import { Button } from 'reactstrap'
import { getCurrentUserDetail, isLoggedIn } from "../../auth"
import { toast } from 'react-toastify';
import {changePassword} from '../../services/user-service'

const ChangePassword = () => {
  const [currentUser, setCurrentUser] = useState(JSON.parse(localStorage.getItem("data")) || null)
  const [login,setLogin]=useState(false);
  const [details,setDetails]=useState({oldPassword:'',newPassword:''})

  useEffect(() => {
    setCurrentUser(getCurrentUserDetail())
    setLogin(isLoggedIn())
  }, [])

  const handleChange = (e) => {
    const {name,value}=e.target
    setDetails(prevState => ({
      ...prevState,
      [name]:value
  }))
  }

  const handleSubmit = () => {
    let obj={...details,email:currentUser.email}
    changePassword(obj).then((data)=>{
      if(data.success){
        toast.success(data.message)
      }
    }).catch((err)=>{
      toast.error(err.response.data.message)
    })
  }

  return (
    <Base>
     <div className="container  bootstrap snippets bootdey" style={{ marginTop: '20px' }}>
      <div className="col-md-9 personal-info">
        <h3>Personal info</h3>

        <form className="form-horizontal" role="form">
          <div className="form-group" style={{ marginBottom: "10px" }}>
            <label className="col-lg-3 control-label">Email:</label>
            <div className="col-lg-8">
              <input
                className="form-control" type="text"
                name="email"
                value={currentUser.email}
                disabled
              />
            </div>
          </div>
          <div className="form-group" style={{ marginBottom: "10px" }}>
            <label className="col-lg-3 control-label">Old Password</label>
            <div className="col-lg-8">
              <input className="form-control"
                name="oldPassword" type="password"
                value={details.oldPassword}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-group" style={{ marginBottom: "10px" }}>
            <label className="col-lg-3 control-label">New Password</label>
            <div className="col-lg-8">
              <input className="form-control" type="password" name="newPassword"
              value={details.newPassword}  onChange={handleChange}
              />
            </div>
          </div>
          <div>
            <Button color="warning" style={{ marginTop: "5px" }} onClick={handleSubmit}>Update Password</Button>

          </div>
        </form>
      </div>
      </div>
    </Base>
  )
}

export default ChangePassword