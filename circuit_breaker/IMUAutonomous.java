package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import java.sql.Timestamp;
import java.time.Instant;


import java.util.Locale;

@Autonomous
public class IMUAutonomous extends LinearOpMode {
    
    BNO055IMU imu = null;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;
    float x_vel = 0, x_pos = 0;
    float timestamp = 0.001f; // 1ms
    
    DcMotor motorFrontLeft = null;
    DcMotor motorBackLeft = null;
    DcMotor motorFrontRight = null;
    DcMotor motorBackRight = null;
    
    @Override public void runOpMode() throws InterruptedException {
        
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("backRight");
        
        // Reverse the right side motors
        // Reverse left motors if you are using NeveRests
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        
        //motorFrontRight.DcMotor$ZeroPowerBehavior = DcMotor
        //motorFrontRight.DcMotor$ZeroPowerBehavior(BRAKE);
        //public static final motorFrontRight.ZeroPowerBehavior;
        //motorFrontRight$ZeroPowerBehavior(BRAKE);
        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        
        // Retrieve the IMU from the hardware map
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        
        // Technically this is the default, however specifying it is clearer
        // Without this, data retrieving from the IMU throws an exception
        imu.initialize(parameters);

        waitForStart();
        
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, this is reversed!
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;
            
            PointAt(90);
        }
    }
    
    void PointAt(double angle){
            if(!opModeIsActive()) return;
             
            // Read inverse IMU heading, as the IMU heading is CW positive
            double botHeading = readHeading();
    
            while(botHeading <= angle - 5 || botHeading > angle + 5){
                if(botHeading <= angle - 5){
                    motorFrontLeft.setPower(-0.25);
                    motorBackLeft.setPower(-0.25);
                    motorFrontRight.setPower(0.25);
                    motorBackRight.setPower(0.25);
                    
                    botHeading = readHeading();
                }else if(botHeading > angle + 5){
                    motorFrontLeft.setPower(0.25);
                    motorBackLeft.setPower(0.25);
                    motorFrontRight.setPower(-0.25);
                    motorBackRight.setPower(-0.25);
                    
                    botHeading = readHeading();
                }
            }
            
            motorFrontLeft.setPower(0);
            motorBackLeft.setPower(0);
            motorFrontRight.setPower(0);
            motorBackRight.setPower(0);
        }
    
        
        void updateTelem(){
            telemetry.addData("Angle", readHeading());
            telemetry.update();
        }
            
        double readHeading(){
            double botHeading = -imu.getAngularOrientation().firstAngle;
            if(botHeading < 0){
                botHeading += 360;
            }
            return botHeading;
        }
}
