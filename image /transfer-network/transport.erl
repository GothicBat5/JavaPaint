-module(wa_raft_transport_sup).
-compile(warn_missing_spec_all).
-behaviour(supervisor).

-export([
    get_or_start/1
]).

-export([
    child_spec/0,
    start_link/0
]).

-export([
    init/1
]).

-spec get_or_start(node()) -> atom().
get_or_start(Node) ->
    Name = wa_raft_transport_target_sup:name(Node),
    not is_pid(whereis(Name)) andalso
        supervisor:start_child(?MODULE, wa_raft_transport_target_sup:child_spec(Node)),
    Name.

-spec child_spec() -> supervisor:child_spec().
child_spec() ->
    #{
        id => ?MODULE,
        start => {?MODULE, start_link, []},
        restart => permanent,
        shutdown => infinity,
        type => supervisor,
        modules => [?MODULE]
    }.

-spec start_link() -> supervisor:startlink_ret().
start_link() ->
    supervisor:start_link({local, ?MODULE}, ?MODULE, []).

-spec init(term()) -> {ok, {supervisor:sup_flags(), [supervisor:child_spec()]}}.
init(_) ->
    {ok, {#{strategy => one_for_one, intensity => 5, period => 1}, []}}.
