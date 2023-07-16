import React from "react"
import { useEffect } from "react"
import { useState } from "react"
import { Link } from "react-router-dom"
import { Button, Card, CardBody, CardFooter, Col, Container, Row, Table } from 'reactstrap'
import { getCurrentUserDetail, isLoggedIn } from "../auth"

const ViewUserProfile = ({ user }) => {

    const [currentUser, setCurrentUser] = useState(null)
    const [login, setLogin] = useState(false)

    useEffect(() => {
        setCurrentUser(getCurrentUserDetail())
        setLogin(isLoggedIn())
    }, [])


    return (
        <Card className='mt-2 border-0 rounded-0 shadow-sm'>
            <CardBody>
                <h3 className='text-uppercase'>User Information</h3>
                <Container className='text-center'>
                    <img style={{ maxWidth: '250px'
                    , maxHeight: '250px' }} src={user.image ? user.image : 'https://cdn.dribbble.com/users/6142/screenshots/5679189/media/1b96ad1f07feee81fa83c877a1e350ce.png?compress=1&resize=400x300&vertical=top'}
                        alt="User profile picture" className='img-fluid rounded-circle' />
                </Container>
                <Table responsive striped hover bordered={true} className='mt-5'>
                    <tbody>
                        <tr>
                            <td>
                                CAMPUS HUB ID
                            </td>
                            <td>
                                CH{user.id}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                USER NAME
                            </td>
                            <td>
                                {user.name}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                USER EMAIL
                            </td>
                            <td>
                                {user.email}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                ABOUT
                            </td>
                            <td>
                                {user.about}
                            </td>
                        </tr>
                        <tr>
                            <td>
                                ROLE
                            </td>
                            <td>
                                {user.roles.map((role) => {
                                    return (
                                        <div key={role.id}>{role.name}</div>
                                    )
                                })}
                            </td>
                        </tr>

                    </tbody>
                </Table>
                {currentUser ? (currentUser.id == user.id) ? (

                    <CardFooter className="text-center mr-2">
                        <Link to={`/user/profile-info/update/${user.id}`}>
                            <Button color="warning">Update Profile</Button>
                        </Link>
                        <Link to={`/user/changePassword`}>
                            <Button  className="ms-2" color="warning">Change Password</Button>
                        </Link>
                    </CardFooter>
                ) : '' : ''}

            </CardBody>
        </Card>

    )
}

export default ViewUserProfile