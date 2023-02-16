package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gyroscope;

@Autonomous

public class IMUFinalForm  extends LinearOpMode {

    private DcMotor backLeft;
    private DcMotor backRight;
    private CRServo clawServo;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private BNO055IMU imu;

    @Override public void runOpMode() throws InterruptedException {
        
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");
        
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        
        // Retrieve the IMU from the hardware map
        BNO055IMU.Parameters parameters = null;
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        
        imu.initialize(parameters);

        waitForStart();
        
        //Start of Auto Instruction
        
        turnTo(90);
    }
    
    void turnTo(double target){
        
        double angle = -imu.getAngularOrientation().firstAngle;
        
        if(angle <= -175 || angle >= 175){
            angle += 360;
            target += 360;
        }
        
        if(angle > target + 5){
            while(angle > target + 5 && opModeIsActive()){
                frontLeft.setPower(0.75);
                backLeft.setPower(0.75);
                frontRight.setPower(-0.75);
                backRight.setPower(-0.75);
                
                angle = -imu.getAngularOrientation().firstAngle;
            }
            while(angle < target - 5 && opModeIsActive()){
                frontLeft.setPower(-0.4);
                backLeft.setPower(-0.4);
                frontRight.setPower(0.4);
                backRight.setPower(0.4);
                
                angle = -imu.getAngularOrientation().firstAngle;
            }
        }else{
            while(angle < target - 5 && opModeIsActive()){
                frontLeft.setPower(-0.75);
                backLeft.setPower(-0.75);
                frontRight.setPower(0.75);
                backRight.setPower(0.75);
                
                angle = -imu.getAngularOrientation().firstAngle;
            }
            while(angle > target + 5 && opModeIsActive()){
                frontLeft.setPower(0.4);
                backLeft.setPower(0.4);
                frontRight.setPower(-0.4);
                backRight.setPower(-0.4);
                
                angle = -imu.getAngularOrientation().firstAngle;
            }
        }
    }
}
