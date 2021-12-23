package com.macro.parking.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.macro.parking.domain.ParkingInfo;
import com.macro.parking.domain.ParkingTicket;
import com.macro.parking.enums.StatusCodeType;

/**
 * 주차 정보 DAO
 */
public interface ParkingInfoDao extends JpaRepository<ParkingInfo, Long>{

	/**
	 * @param cur 예약시간
	 * @return ParkingInfo
	 *  해당 예약시간 이후에 예약된 주차 정보
	 */
	ParkingInfo findTopByOrderTimeGreaterThanEqual(LocalDateTime cur);

	/**
	 * @param cur
	 * @return ParkingInfo
	 *  현재 가장 최신 주차 정보를 가져온다.
	 */
	ParkingInfo findTopByOrderTimeGreaterThanEqualOrderByOrderTimeDescParkingInfoIdDesc(LocalDateTime cur);

	/**
	 * @param parkingInfoId 주차 정보 기본키
	 * @return ParkingInfo
	 *  입력받은 기본키에 해당하는 주차 정보를 가져온다.
	 */
	ParkingInfo findByParkingInfoId(int parkingInfoId);

	/**
	 * @param appFlags 주차 정보 상태
	 * @param cur 오늘
	 * @return List<ParkingInfo>
	 *     오늘 예약된 주차 정보 중 인자로 넘겨 받은 주차권 상태 중 하나인 주차권들을 모두 가져온다.
	 */
	List<ParkingInfo> findByAppFlagInAndOrderTimeGreaterThanEqual(List<StatusCodeType> appFlags, LocalDateTime cur);

	/**
	 * @param appFlags 주차권 상태 정보
	 * @param parkingTickets 주차권
	 * @param cur 오늘
	 * @return 오늘 예약된 주차 정보 중 인자로 넘겨 받은 주차권이 인자로 넘겨 받은 주차권 상태 정보 중 하나일 경우 모두 가져온다.
	 */
	List<ParkingInfo> findByAppFlagInAndParkingTicketInAndOrderTimeGreaterThanEqual(List<StatusCodeType> appFlags,List<ParkingTicket> parkingTickets, LocalDateTime cur);

	/**
	 * @param cur 오늘
	 * @return List<ParkingInfo>
	 *     오늘 예약된 주차 정보를 모두 가져온다
	 */
	List<ParkingInfo> findByOrderTimeGreaterThanEqual(LocalDateTime cur);

	/**
	 * @param cur 오늘
	 * @param parkingTickets 주차권
	 * @param carNumber 차량 넘버
	 * @return List<ParkingInfo>
	 *     오늘 예약된 주차 정보 중 해당 주차권과 차량 넘버가 같은 주차 정보를 가져온다.
	 */
	List<ParkingInfo> findByOrderTimeGreaterThanEqualAndParkingTicketInOrCar_NumberLike(LocalDateTime cur, List<ParkingTicket> parkingTickets, String carNumber);

}