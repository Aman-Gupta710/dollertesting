import React, { useState } from 'react'
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
import { Link } from 'react-router-dom';
import { sendResetEmail } from '../../services/user-service';
import { toast } from 'react-toastify';

const ResetPassword = () => {
    const [email,setEmail]=useState('')

    const handleChange=(e)=>{
        setEmail(e.target.value)
    }

    const handleSubmit=()=>{
        sendResetEmail({email:email}).then((res)=>{
            toast.success(res.message)
        }).catch((err)=>{
            toast.error(err.response.data.message)
        })
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
                                <h3>Reset Password!!</h3>
                            </CardHeader>

                            <CardBody>
                                <Form >
                                    {/* Email field */}

                                    <FormGroup>
                                        <Label for="email">Enter Email</Label>
                                        <Input
                                            type="text"
                                            id="email"
                                            name="email"
                                            value={email}
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

export default ResetPassword