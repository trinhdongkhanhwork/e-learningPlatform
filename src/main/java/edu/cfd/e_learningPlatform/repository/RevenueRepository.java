package edu.cfd.e_learningPlatform.repository;

import edu.cfd.e_learningPlatform.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenueRepository extends JpaRepository<Payment, Long> {

    @Query("select \n" +
            "        case when :startDate is not null and :endDate is not null then DATE_FORMAT(p.paymentDate, '%Y-%m-%d') \n" +
            "             else MONTHNAME(p.paymentDate) end as label, \n" +
            "        coalesce(count(distinct p.course.id), 0) as totalCoursesSold,\n" +
            "        coalesce(count(distinct p.user.id), 0) as totalUsers,\n" +
            "        coalesce(sum(p.price), 0) as totalRevenue\n" +
            "    from Payment p\n" +
            "    where (:startDate is null or p.paymentDate >= :startDate) \n" +
            "      and (:endDate is null or p.paymentDate <= :endDate)\n" +
            "      and p.paymentStatus.id = 1\n" +
            "    group by case when :startDate is not null and :endDate is not null then DATE_FORMAT(p.paymentDate, '%Y-%m-%d') \n" +
            "             else MONTH(p.paymentDate) end, \n" +
            "             case when :startDate is not null and :endDate is not null then DATE_FORMAT(p.paymentDate, '%Y-%m-%d') \n" +
            "             else MONTHNAME(p.paymentDate) end\n" +
            "    order by min(p.paymentDate)")
    List<Object[]> getRevenueStatistics(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT months.monthOrder, " +
            "       months.monthName, " +
            "       COALESCE(b.totalCoursesBought, 0) AS totalCoursesBought, " +
            "       COALESCE(b.totalSpent, 0) AS totalSpent, " +
            "       COALESCE(b.userName, 'No Data') AS userName " +
            "FROM (SELECT 1 AS monthOrder, 'January' AS monthName " +
            "      UNION ALL " +
            "      SELECT 2, 'February' " +
            "      UNION ALL " +
            "      SELECT 3, 'March' " +
            "      UNION ALL " +
            "      SELECT 4, 'April' " +
            "      UNION ALL " +
            "      SELECT 5, 'May' " +
            "      UNION ALL " +
            "      SELECT 6, 'June' " +
            "      UNION ALL " +
            "      SELECT 7, 'July' " +
            "      UNION ALL " +
            "      SELECT 8, 'August' " +
            "      UNION ALL " +
            "      SELECT 9, 'September' " +
            "      UNION ALL " +
            "      SELECT 10, 'October' " +
            "      UNION ALL " +
            "      SELECT 11, 'November' " +
            "      UNION ALL " +
            "      SELECT 12, 'December') months " +
            "LEFT JOIN (SELECT FUNCTION('MONTH', p.paymentDate) AS monthOrder, " +
            "                 p.user.fullname AS userName, " +
            "                 COUNT(p.course.id) AS totalCoursesBought, " +
            "                 SUM(p.price) AS totalSpent " +
            "           FROM Payment p " +
            "           WHERE p.paymentDate BETWEEN :startDate AND :endDate " +
            "           AND p.paymentStatus.id = 1 " +
            "           GROUP BY FUNCTION('MONTH', p.paymentDate), p.user.fullname) b " +
            "ON months.monthOrder = b.monthOrder " +
            "ORDER BY months.monthOrder")
    List<Object[]> getAllMonthsWithTopBuyers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    @Query("SELECT p.course.id AS courseId, p.course.title, COUNT(p) AS totalSold, p.course.coverImage, SUM(p.price) " +
            "FROM Payment p " +
            "WHERE p.paymentStatus.id = 1 " +
            "GROUP BY p.course.id, p.course.title, p.course.coverImage " +
            "ORDER BY totalSold DESC LIMIT 5")
    List<Object[]> getTopCourses();
}
