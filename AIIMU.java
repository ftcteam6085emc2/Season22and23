package org.firstinspires.ftc.teamcode;
// Import the necessary libraries
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
@Autonomous
public class AIIMU extends LinearOpMode {
// Declare and initialize necessary variables
    double targetDistance = 10; // inches
    double wheelCircumference = 4 * Math.PI; // inches
    double speed = 0.5; // motor power
    double error;
    double Kp = 0.005; // proportional constant
    @Override public void runOpMode() throws InterruptedException {
        // Declare and initialize hardware devices
        DcMotor motorFrontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor motorFrontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor motorBackLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor motorBackRight = hardwareMap.get(DcMotor.class, "backRight");
        BNO055IMU imu = hardwareMap.get(BNO055IMU.class, "imu");
        
        // Set the direction of the motors
        motorFrontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorFrontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBackRight.setDirection(DcMotorSimple.Direction.REVERSE);
        
        // Initialize the IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;
        imu.initialize(parameters);
        
        // Set the motors to the desired power
        motorFrontLeft.setPower(speed);
        motorFrontRight.setPower(speed);
        motorBackLeft.setPower(speed);
        motorBackRight.setPower(speed);
        
        ElapsedTime runtime = new ElapsedTime();
        
        // Use the IMU to keep the robot going straight
        while (runtime.seconds() < targetDistance / (speed * wheelCircumference)) {
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            error = angles.firstAngle;
            double correction = error * Kp;
            motorFrontLeft.setPower(speed + correction);
            motorFrontRight.setPower(speed - correction);
            motorBackLeft.setPower(speed + correction);
            motorBackRight.setPower(speed - correction);
        }
        
        // Stop the motors
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
    }
}