package com.example.uploadtest

import cats.effect.Async
import cats.effect.kernel.Resource
import com.comcast.ip4s._
import fs2.io.net.Network
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.{EntityDecoder, HttpApp}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object UploadtestServer {

  def blazeServer[F[_] : Async](httpApp: HttpApp[F]) = BlazeServerBuilder[F]
    .bindHttp(host = "0.0.0.0", port = 8080)
    .withHttpApp(httpApp)
    .resource

  def emberServer[F[_]: Async: Network](httpApp: HttpApp[F]) = EmberServerBuilder.default[F]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build

  def run[F[_]: Async: fs2.io.file.Files]: F[Nothing] = {
    for {
      multipartDecoder <- EntityDecoder.mixedMultipartResource()
      routes <- Resource.pure(new UploadtestRoutes[F](multipartDecoder).routes)

      httpApp = (
        routes
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <- blazeServer(finalHttpApp)
    } yield ()
  }.useForever
}
