/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wvlet.airframe.jdbc
import wvlet.airframe.Design
import wvlet.airframe.codec.MessageCodec
import wvlet.airspec.AirSpec

/**
  *
  */
class DbConfigTest extends AirSpec {
  test("support serde") {
    val db    = DbConfig.ofSQLite("mydb.sqlite")
    val codec = MessageCodec.of[DbConfig]
    val json  = codec.toJson(db)
    debug(json)
    val db2 = codec.fromJson(json)
    debug(db2)
  }

  test("support serde of ConnectionPoolConfig") {
    val c     = ConnectionPoolConfig()
    val codec = MessageCodec.of[ConnectionPoolConfig]
    val json  = codec.toJson(c)
    info(c)
    info(json)
    val c1 = codec.fromJson(json)
    info(c1)
  }

  test("bind config") {
    import wvlet.airframe.config._
    val d = Design.newSilentDesign
      .bindConfigFromYaml[DbConfig]("dbconfig.yml")
      .overrideConfigWithPropertiesFile("config.properties")

    val configList = d.currentConfig.printConfig
    info(configList)

    d.build[DbConfig] { c => debug(c) }
  }

}