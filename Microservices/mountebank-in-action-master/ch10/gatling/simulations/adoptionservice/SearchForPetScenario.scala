package adoptionservice

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class SearchForPetSimulation extends Simulation {
  val httpProtocol = http
    .baseURL("http://localhost:5000")

  val searchScenario = scenario("SearchForPetSimulation")
    .exec(http("first search")
      .get("/nearbyAnimals?postalCode=75228&maxDistance=20"))
    .pause(10)
    .exec(http("second search")
      .get("/nearbyAnimals?postalCode=75228&maxDistance=50"))
    .pause(15)
    .exec(http("first animal")
      .get("/animals/10677691"))
    .pause(5)
    .exec(http("second animal")
      .get("/animals/10837552"))
    .pause(5)
    .exec(http("third animal")
      .get("/animals/11618347"))

  setUp(
    searchScenario.inject(rampUsers(3125) over (10 seconds))
  ).protocols(httpProtocol)
}
