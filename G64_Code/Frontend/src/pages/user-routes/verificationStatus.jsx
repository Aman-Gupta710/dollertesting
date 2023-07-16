import React,{useEffect,useState} from 'react'
import {useSearchParams} from 'react-router-dom'
import { verifyToken } from '../../services/user-service';

const VerificationStatus = () => {
    const [searchParams] = useSearchParams();
    const [loading,setLoading]=useState(false);
    const [success,setSuccess]=useState(false);
    const [message,setMessage]=useState('')
    useEffect(()=>{
        setLoading(true)
        const token=searchParams.get('token')
        verifyToken(token).then((res)=>{
            if(res.success){
                setLoading(false)
                setSuccess(res.success)
                setMessage(res.message)
            }
        }).catch((err)=>{
            setMessage(err.response.data.message)
        })
    },[])
  return (
    <div 
        style={
            {fontSize:'24px',display:'flex',
            justifyContent:'center',fontStyle:'bold'
        }}
    >
        {
            !loading && success?message:message
        }
    </div>
  )
}

export default VerificationStatus