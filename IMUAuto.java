/*package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous

public class IMUAuto {

    private DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
    private DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
    private DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
    private DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");
    
    private DcMotor linearSlide = hardwareMap.get(DcMotor.class, "linearSlide");
    
    private Servo clawServo = null;
    
    private double maxDrivePower = 1;
    private double slideSpeed = 0.75;
    
    private double offset = 0.322;
    private double clawClose = 0;
    private double clawOpen = 0.2;
    
    frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        
        linearSlide = hardwareMap.get(DcMotor.class, "linearSlide");
        
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);
}*/