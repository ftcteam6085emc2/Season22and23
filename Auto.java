package org.firstinspires.ftc.teamcode;

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

public class Auto extends LinearOpMode {
    //rivate Gyroscope imu;
    
    private CRServo clawServo;
    
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private ElapsedTime runtime = new ElapsedTime();
    
    public void runOpMode() {
        //imu = hardwareMap.get(Gyroscope.class, "imu");
        motorBackLeft = hardwareMap.get(DcMotor.class, "backLeft");
        motorBackRight = hardwareMap.get(DcMotor.class, "backRight");
        motorFrontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        motorFrontRight = hardwareMap.get(DcMotor.class, "frontRight");
        //claw = hardwareMap.get(Servo.class, "claw");
        
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()){
            telemetry.addData("Status", "Running");
            telemetry.update();
            while(runtime.seconds() <= 1.0){
                
                motorFrontLeft.setPower(-0.5);
                motorBackLeft.setPower(-0.5);
                motorFrontRight.setPower(-0.5);
                motorBackRight.setPower(-0.5);
                
            }
            runtime.reset();
            while(runtime.seconds() <= 2.0){
                
                motorFrontLeft.setPower(0.5);
                motorBackLeft.setPower(-0.5);
                motorFrontRight.setPower(0.5);
                motorBackRight.setPower(-0.5);
                
            }
            
            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackRight.setPower(0);
        }
    }
}