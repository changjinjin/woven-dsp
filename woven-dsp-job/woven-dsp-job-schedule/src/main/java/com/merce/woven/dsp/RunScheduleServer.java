package com.merce.woven.dsp;


/**
 * @Author: haijun
 * @Date: 2019/12/12 14:17
 */
//@Configuration
//@EnableAutoConfiguration
//@EnableFeignClients
//@EnableEurekaClient
public class RunScheduleServer {
//    private static Logger logger = LoggerFactory.getLogger(RunScheduleServer.class);
//    // cache 系统内嵌的处理器
//    private static Map<String, Class<? extends JobSchedule>> pushApis = new HashMap<>();
//
//    public static void main(String[] args) {
//
//        new FastClasspathScanner("com.merce.woven.dsp.scheduler").matchClassesImplementing(JobSchedule.class, aClass -> {
//            if (!Modifier.isAbstract(aClass.getModifiers())) {
//                try {
//                    JobSchedule job = aClass.newInstance();
//                    pushApis.put(job.getJobType(), aClass);
//                    logger.info("internal JobSchedule class loaded: " + job.getJobType());
//                } catch (Exception e) {
//                    logger.error("load internal JobSchedule " + aClass.getName() + " failed: " + e.getMessage() + ", ignored.");
//                }
//            }
//        }).scan();
//
//        System.setProperty(
//                "spring.config.location", "classpath:woven.properties,classpath:woven-dsp-job.properties");
//
//        new SpringApplicationBuilder()
//                .bannerMode(Mode.OFF)
//                .properties()
//                .web(WebApplicationType.SERVLET)
//                .sources(RunScheduleServer.class)
//                .run(args);
//    }
}
