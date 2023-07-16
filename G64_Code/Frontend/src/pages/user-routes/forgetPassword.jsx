import React,{useState} from 'react'
import Base from '../../components/Base'
import {
    Label,
    Card,
    CardBody,
    CardHeader,
    Col,
    Container,
    Form,
    FormGroup,
    Input,
    Row,
    Button,
} from "reactstrap";
import { toast } from 'react-toastify';
import {resetPassword} from '../../services/user-service'
import { useParams } from 'react-router-dom';

const ForgetPassword = () => {
    const {token}=useParams()
    const [details,setDetails]=useState({password:'',confirmPassword:''})

    const handleChange=(e)=>{
        const {name,value}=e.target;
        setDetails(prevState => ({
            ...prevState,
            [name]:value
        }))
    }

    const handleSubmit=()=>{
        if(details.password!==details.confirmPassword){
            toast.error('Confirm Password Not matched with password')
        }else{
            let password={newPassword:details.password}
            resetPassword(password,token).then((data)=>{
                if(data.success){
                    toast.success(data.message)  
                }else{
                    toast.error(data.message)  
                }
            }).catch((err)=>{
                toast.error(err.response.data.message)
            })
        }
    }
  return (
    <Base>
         <Container>
                <Row className="mt-4">
                    <Col
                        sm={{
                            size: 6,
                            offset: 3,
                        }}
                    >
                        <Card color="dark" inverse>
                            <CardHeader>
                                <h3>Change Password!!</h3>
                            </CardHeader>

                            <CardBody>
                                <Form >
                                    {/* Email field */}

                                    <FormGroup>
                                        <Label for="password">New Password</Label>
                                        <Input
                                            type="password"
                                            id="password"
                                            name="password"
                                            value={details.password}
                                            onChange={handleChange}
                                        />
                                    </FormGroup>
                                    <FormGroup>
                                        <Label for="password">Confirm Password</Label>
                                        <Input
                                            type="password"
                                            id="confirmPassword"
                                            name="confirmPassword"
                                            value={details.confirmPassword}
                                            onChange={handleChange}
                                        />
                                    </FormGroup>
                                    <Container className="text-center">
                                        <Button color="light" outline onClick={handleSubmit}>
                                            Submit
                                        </Button>
                                    </Container>
                                </Form>
                            </CardBody>
                        </Card>
                    </Col>
                </Row>
            </Container>
    </Base>
  )
}

export default ForgetPassword