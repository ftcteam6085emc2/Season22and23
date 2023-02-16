package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;
import java.time.Instant;

@Autonomous
public class RedAutoEncoderCB extends LinearOpMode {
    boolean scanned = true;
    int tZone = 0;
    double targetHeading = 0;
    double currentHeading = 0;
    String heading = "0";
    
    private double offset = 0.322;
    private double clawOpen = 0;
    private double clawClose = 0.2;

    Orientation angles;
    Acceleration gravity;

    private BNO055IMU imu;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private Servo clawServo = null;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = false;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        
        imu.initialize(parameters);
        
        clawServo = hardwareMap.get(Servo.class, "clawServo");
        
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        frontRight.setTargetPosition(0);
        frontLeft.setTargetPosition(0);
        backRight.setTargetPosition(0);
        backLeft.setTargetPosition(0);
        
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        waitForStart();
        
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        
        clawServo.setPosition(clawClose + offset);
        sleep(500);
        DriveStraightDistance(50, 0.5);
        sleep(100);
        turnTo(-90);
        sleep(100);
        DriveStraightDistance(1200, 0.5);
        sleep(100);
        clawServo.setPosition(clawOpen + offset);
        sleep(100);
        DriveStraightDistance(-1000, 0.5);
        sleep(100);
        turnTo(0);
        sleep(100);
        DriveStraightDistance(1000, 0.5);
        sleep(100);
    }

    private void DriveStraight(double power) {
        frontRight.setPower(power);
        frontLeft.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);
    }

    private void StopDriving() {
        DriveStraight(0);
    }

    private void DriveStraightDistance(int distance, double power) {
        telemetry.update();
        
        frontRight.setTargetPosition(frontRight.getCurrentPosition() + distance);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() + distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() + distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + distance);
        
        DriveStraight(power);
        while ((frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy()) && opModeIsActive()) {
            idle();
        }

        StopDriving();
    }

    private void Strafe(int distance, double power) {
        telemetry.update();

        frontRight.setTargetPosition(frontRight.getCurrentPosition() + distance);
        frontLeft.setTargetPosition(frontLeft.getCurrentPosition() - distance);
        backRight.setTargetPosition(backRight.getCurrentPosition() - distance);
        backLeft.setTargetPosition(backLeft.getCurrentPosition() + distance);

        DriveStraight(power);
        while ((frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy()) && opModeIsActive()) {
            idle();
        }

        StopDriving();
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
    
    void turnTo(double target){
        target *= -1;
        
        double lowPower = 0.35;
        double highPower = 0.5;
        
        double angle = -imu.getAngularOrientation().firstAngle;
        
        boolean lAxis = false;
        
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        if(angle <= -175 || angle >= 175){
            lAxis = true;
            angle += 360;
            target += 360;
        }
        
        if(angle < target + 5){
            while(angle < target + 5 && opModeIsActive()){
                frontLeft.setPower(-highPower);
                backLeft.setPower(-highPower);
                frontRight.setPower(highPower);
                backRight.setPower(highPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
            while(angle > target + 5 && opModeIsActive()){
                frontLeft.setPower(lowPower);
                backLeft.setPower(lowPower);
                frontRight.setPower(-lowPower);
                backRight.setPower(-lowPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
        }else{
            while(angle > target - 5 && opModeIsActive()){
                frontLeft.setPower(highPower);
                backLeft.setPower(highPower);
                frontRight.setPower(-highPower);
                backRight.setPower(-highPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
            while(angle < target - 5 && opModeIsActive()){
                frontLeft.setPower(-lowPower);
                backLeft.setPower(-lowPower);
                frontRight.setPower(lowPower);
                backRight.setPower(lowPower);
                
                angle = imu.getAngularOrientation().firstAngle;
                
                if(lAxis){
                    angle += 360;
                }
                
                telemetry.addData("Angle", angle);
                telemetry.update();
            }
        }
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
