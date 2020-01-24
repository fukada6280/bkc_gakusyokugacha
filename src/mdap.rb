# -*- coding:utf-8 -*-
require 'time'


def mdap(n, desc:nil, bar_shape:["\e[42m \e[0m", "─"],
         indicator:"⠻⠽⠾⠷⠯⠟", datetime_format:"%M:%S"
        )
  ## -----*----- Progress Bar -----*----- ##
  # ステータス
  out_fmt = "[] %s%d%% (%d/%d) [%s-%s %fit/s]"
  c_indicator = ''
  i_indicator = 0
  progress = 0
  dt_start = Time.new
  dt_now = Time.new

  outer = lambda {
    width = `tput cols`.to_i
    dt_now = Time.new
    str = sprintf(out_fmt, c_indicator,
                  progress*100/n, progress, n,
                  dt_start.strftime(datetime_format),
                  dt_now.strftime(datetime_format),
                  progress / (dt_now-dt_start)
                 )
    out_str = "#{desc}: #{out_str}"  unless desc.nil?

    # 進捗バー
    bar_len= width-str.length
    bar = (1..bar_len).map { |i|
      if progress/n.to_f >= i/bar_len.to_f
        bar_shape[0]
      else
        bar_shape[1]
      end
    }.join

    # 表示
    print "\r#{str.gsub('[]', "[#{bar}]")}"
  }

  # タイマー設定（インジケータ用）
  unless indicator.nil?
    Timer::set_frame_rate(500)
    i_indicator = 0

    # インジケータ文字を切り替え
    Timer::timer {
      i_indicator += 1
      c_indicator = indicator[i_indicator%indicator.length]
      outer.call
    }
  end

  1.upto(n) do |i|
    begin
      progress = i
      outer.call
      yield i-1
    rescue
      Timer::exit
    end
  end

  Timer::exit
end


module Timer
  def set_frame_rate(time)
    ## -----*----- フレームレートの初期化 -----*----- ##
    @frame_rate = time
  end

  def timer(join: false, sleep: true)
    ## -----*----- タイマー設定（サブスレッド） -----*----- ##
    @th = Thread.new {
      loop do
        yield
        sleep 60.0 / @frame_rate if sleep
      end
    }
    @th.join if join
  end

  def exit
    ## -----*----- タイマー処理終了 -----*----- ##
    @th.kill
  end

  module_function :set_frame_rate, :timer, :exit
end
