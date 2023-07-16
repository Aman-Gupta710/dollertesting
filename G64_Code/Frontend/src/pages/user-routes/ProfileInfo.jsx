import React, { useContext } from 'react'
import { useState } from 'react'
import { useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { Card, CardBody, Col, Container, Row, Table } from 'reactstrap'
import Base from '../../components/Base'
import ViewUserProfile from '../../components/ViewUserProfile'
import userContext from '../../context/userContext'
import { getUser } from '../../services/user-service'
import { deletePostService, loadPostUserWise } from '../../services/post-service'
import { toast } from 'react-toastify'
import Post from '../../components/Post'
import { getCurrentUserDetail } from '../../auth'

function ProfileInfo() {
  const object = useContext(userContext)
  const [posts, setPosts] = useState([])

  const [user, setUser] = useState(null)
  const { userId } = useParams()
  // console.log(userId);
  useEffect(() => {
    getUser(userId).then(data => {
      console.log(data);
      setUser({ ...data })
    })
    loadPostData(userId)
  }, [userId])

  function loadPostData(id) {
    loadPostUserWise(id).then(data => {
      console.log('data',data)
      setPosts([...data.reverse()])
    })
      .catch(error => {
        console.log(error)
        toast.error("error in loading user posts")
      })
  }

  function deletePost(post) {
    //going to delete post
    console.log(post)

    deletePostService(post.postId).then(res => {
      console.log(res)
      toast.success("post is deleled..")
      let newPosts = posts.filter(p => p.postId != post.postId)
      setPosts([...newPosts])

    })
      .catch(error => {
        console.log(error)
        toast.error("error in deleting post")
      })
  }

  const userView = () => {
    return (
      <Row>
        <Col md={{ size: 6, offset: 3 }}>

          <ViewUserProfile user={user} />
          <h2 style={{marginTop:'10px'}}>My Posts</h2>
          {posts.map((post, index) => {
            return (
              <Post post={post} deletePost={deletePost} key={index} />
            )
          })}
        </Col>
      </Row>
    )
  }

  return (
    <Base>
      {user ? userView() : 'Loading user data...'}

    </Base>
  )
}

export default ProfileInfo