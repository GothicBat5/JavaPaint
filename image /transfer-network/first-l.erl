-module(math).
-export([factorial/1, area/1]).
-module(ping_pong).
-export([start/0, ping/2, pong/0]).

area({circle, R}) -> math:pi() * R * R; % tuple pattern
area({rect, W, H}) -> W * H.

ping(0, Pong_PID) ->
    Pong_PID! finished;
ping(N, Pong_PID) ->
    Pong_PID! {ping, self()},
    receive
        pong -> io:format("Ping ~p~n", [N])
    end,
    ping(N-1, Pong_PID).

factorial(0) -> 1; % pattern match
factorial(N) when N > 0 ->
    N * factorial(N-1).

pong() ->
    receive
        finished -> io:format("Pong done~n");
        {ping, Ping_PID} ->
            io:format("Pong ~n"),
            Ping_PID! pong,
            pong()
    end.

start() ->
    Pong_PID = spawn(ping_pong, pong, []),
    spawn(ping_pong, ping, [3, Pong_PID]).
