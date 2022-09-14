package com.ntg.sadmin.web.services;
import com.ntg.sadmin.data.service.EmployeeEntityService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;
import java.util.Objects;
import org.smpp.Data;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.pdu.Address;
import org.smpp.pdu.BindRequest;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.SubmitSM;
import org.smpp.pdu.SubmitSMResp;
import org.smpp.pdu.WrongLengthOfStringException;


@Service
public class SmppService {

//    @Autowired
//   private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private EmployeeEntityService employeeEntityService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SmppService.class);

    public HashMap<String,String> sendVerifyCode(String user_name, String twoFaCode, String NumberEnterCode, String host, String port, String userName, String password) {
        String destination = employeeEntityService.findMobileFactorAuthCode(user_name);
           HashMap<String,String> send= new HashMap<>();
//        Store key in Redis
        String key = getDestinationNumKey(destination);
//        The verification code exists in the Redis, the verification code stored in REDIS (sends the same verification code within 15 minutes), otherwise the 6-digit verification code is generated.
//        Object valueByKey = getValueByKey(key);
//        String verificationCode = Objects.nonNull(valueByKey) ? valueByKey.toString() : String.valueOf((int) (Math.random() * 900000 + 100000));
//        Tenant (no pass, default Default)
        if(destination !=null){
            boolean sendSuccess = sendMessage(host, port, userName, password, destination, twoFaCode);
            if (sendSuccess) {
                //After the transmission is successful, the verification code is deposited in Redis and sets expiration time (default 15 minutes)
//                 putValueToRedisWithTimeout(key, twoFaCode, 15, TimeUnit.MINUTES);
               send.put("Sending","Yes");
            }else{
                send.put("Sending","No");
            }
            send.put("isValidNumber","Yes");

        }else{
            send.put("isValidNumber","No");
        }
        return send;
    }
    private String getDestinationNumKey(String destination) {
        return String.format("%s:%s", "DESTINATION", destination);
    }
    public boolean sendMessage(String host,
                               String port,
                               String userName,
                               String password,
                               String phonenumber,
                               String verifyCode) {
        LOGGER.info("start to send sms notification, reciever,host {},port {}, userName {} password {} destinations is {} verifyCode {}", host, port, userName, password, phonenumber, verifyCode);
        try {
            TCPIPConnection connection = new TCPIPConnection(host, Integer.parseInt(port));
            Session session = new Session(connection);
            BindRequest request = new BindTransmitter();
            request.setSystemId(userName);
            request.setPassword(password);
//SMPP protocol version
            request.setInterfaceVersion((byte) 0x34);
            request.setSystemType("");
            BindResponse bind = session.bind(request);
            LOGGER.info("bind response debugString {},response command status {}", bind.debugString(), bind.getCommandStatus());
            String content = "[Registration]" + verifyCode + " is your verification code. Valid in 15 minutes. Please do not share this code with anyone else.";
            SubmitSM submitSM = constructRequest(phonenumber, content);
//Bund Faild causes TCPIPConnection to shut down to cause OutputStream to shut down to cause NO
            SubmitSMResp response = session.submit(submitSM);
            LOGGER.info("send message result {},command status is {}", response.debugString(), response.getCommandStatus());
            return true;
        } catch (Exception e) {
            LOGGER.error("invoke sms session exception", e);
            e.printStackTrace();
            return false;
        }

    }
    private SubmitSM constructRequest(String phoneNumber, String content) throws WrongLengthOfStringException, UnsupportedEncodingException {
        String recipientPhoneNumber = phoneNumber;
        SubmitSM request = new SubmitSM();
       // request.setSourceAddr(createAddress("MelroseLabs"));
        request.setDestAddr(createAddress(recipientPhoneNumber));
        request.setShortMessage(content, Data.ENC_UTF8);
        request.setReplaceIfPresentFlag((byte) 0);
        request.setEsmClass((byte) 0);
        request.setProtocolId((byte) 0);
        request.setPriorityFlag((byte) 0);
        request.setRegisteredDelivery((byte) 1);// we want delivery reports
        request.setDataCoding((byte) 0);
        request.setSmDefaultMsgId((byte) 0);
        return request;
    }
    private Address createAddress(String address) throws WrongLengthOfStringException {
        Address addressInst = new Address();
        // national ton
        addressInst.setTon((byte) 5);
        // numeric plan indicator
        addressInst.setNpi((byte) 0);
        addressInst.setAddress(address, Data.SM_ADDR_LEN);
        return addressInst;
    }
//    public void putValueToRedisWithTimeout(Object key, Object value, final long timeout, final TimeUnit unit) {
//        try {
//            ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
//            valueOperations.set(key, value, timeout, unit);
//        } catch (Exception e) {
//        }
//
//    }
//    public Object getValueByKey(Object key) {
//        Object value = null;
//        try {
//            ValueOperations<Object, Object> valueOperations = redisTemplate.opsForValue();
//            value = valueOperations.get(key);
//        } catch (Exception e) {
//        }
//        return value;
//    }
//    public Object deleteKey(Object key) {
//        Object value = null;
//        try {
//            if (redisTemplate.hasKey(key)) {
//                redisTemplate.delete(key);
//            }
//        } catch (Exception e) {
//        }
//        return value;
//    }

}
