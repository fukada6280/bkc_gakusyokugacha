# -*- coding:utf-8 -*-
require 'fileutils'
require 'open-uri'
require 'husc'
require 'mdap'

def webDL(link, file)
  ## -----*----- Webからダウンロード -----*----- ##
  FileUtils.mkdir_p(File.dirname(file)) unless FileTest.exist?(File.dirname(file))
  begin
    open(file, 'wb') do |local_file|
      URI.open(link) do |web_file|
        local_file.write(web_file.read)
      end
    end

  rescue => e
    print e
  end
end

if __FILE__ == $0
  url = 'https://www.u-coop.net/food/menu/hanshin/info.php'
  agent = Husc.new(url)

  # fetch all categories
  categories = agent.css('#nav').css('li').map do |el|
    url + el.css('a').attr('href')
  end

  # fetch image links and names
  images = []
  categories.each do |category|
    doc = Husc.new(category)
    doc.css('img').each { |el|
      unless el.attr('alt') == ""
        images <<  {
          name: el.attr('alt'),
          link: 'https://www.u-coop.net/food/menu/' + el.attr('src').gsub('../', '')
        }
      end
    }
  end

  # download image files
  mdap(images.length) do |i|
    link = images[i][:link]
    path = 'img/' + images[i][:name] + '.png'
    webDL(link, path)
  end
end
