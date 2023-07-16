import userContext from "../context/userContext";
import Base from "../components/Base";

const About = () => {
  return (
    <userContext.Consumer>
      {(object) => (
        <Base>
          <center>
            <h1 style={{ marginTop: "10px" }}>About Us</h1>
          </center>
          <div style={{ padding: "30px" }}>

            <h1 style={{ marginBottom: "20px", color: "#f44b4b" }}>Welcome to Campus Hub</h1>
            <h5 style={{ lineHeight: "30px", fontSize: "22px", fontWeight: 400, wordWrap: "wrap" }}>
              This web-app is developed for all the students, alumni students,
              teachers, management, and other authorities. It provides a common
              ground for all the departments to share their updates which reaches
              to students immediately.
            </h5>
            <h5 style={{ lineHeight: "30px", fontSize: "22px", fontWeight: "400" }}>
              Any one who want to share his/her ideas to whole college can do a
              post on this platform. Rest we will make sure it reaches to everyone
              by sending a notification email to everyone about your post.
            </h5>

            <h6 style={{ lineHeight: "30px", fontSize: "22px", fontWeight: 400 }}>
              If you have any query or feedback, you can mail us @ : <span
                style={{ fontWeight: 500 }}>hello.campushub@gmail.com</span>
            </h6>
            <h6 style={{ fontSize: "20px", fontWeight: 400, paddingTop: "10px" }}>Thanks</h6>
            <h6 style={{ fontSize: "20px", fontWeight: 400, paddingTop: "10px" }}>Team CampusHub</h6>
          </div>
        </Base>
      )}
    </userContext.Consumer>
  );
};

export default About;
